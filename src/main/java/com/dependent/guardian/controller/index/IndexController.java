package com.dependent.guardian.controller.index;

import com.dependent.guardian.entity.address.Address;
import com.dependent.guardian.entity.address.AddressMap;
import com.dependent.guardian.entity.member.Member;
import com.dependent.guardian.entity.worker.Worker;
import com.dependent.guardian.service.address.AddressService;
import com.dependent.guardian.service.function.KakaoService;
import com.dependent.guardian.service.function.NaverService;
import com.dependent.guardian.service.member.MemberService;
import com.dependent.guardian.service.worker.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

//@SessionAttributes를 알기 이전 코드라서 안쓰고 그대로 뒀음
@Controller
@RequestMapping("/index")
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final WorkerService workerService;
    private final MemberService memberService;
    private final AddressService addressService;

    //여기서 session.setAttribute()를 많이 쓴이유?
    // - redirectAttributes.addFlashAttribute()가 어떤경우에도 안되서 어쩔 수 없음

    //첫페이지 - 자동 로그인 검사
    @GetMapping
    public String indexPage(@CookieValue(value = "workerId", required = false) String workerId,
                            @CookieValue(value = "memberId", required = false) String memberId,
                            HttpSession session, Model model) {
        log.info("indexPage : workerId = {}, memberId = {}", workerId, memberId);

        if (workerId != null) {
            session.setAttribute("id", workerId);
            return "redirect:/index/signIn";
        } else if (memberId != null) {
            session.setAttribute("id", memberId);
            return "redirect:/index/signIn";
        } else {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            model.addAttribute("uuid", uuid);

            return "front/index";
            //쿠키가 없다면 sns 선택 화면 보여줌
        }
    }

    //보호자, 요보사선택화면
    @GetMapping("/accountChoice")
    public String accountChoice() {
        log.info("accountChoice");

        return "front/index/accountChoice";
    }

    //로그인, 자동로그인 쿠키 등록
    @GetMapping("/signIn")
    public String signIn(HttpServletResponse response, HttpSession session) {
        String id = (String) session.getAttribute("id");
        log.info("signIn : id = {}", id);

        Cookie cookie;

        if (id != null && !id.equals("")) {
            Optional<Worker> worker = workerService.signIn(id);
            Optional<Member> member = memberService.signIn(id);
            if (worker.isPresent()) {
                session.setAttribute("worker", worker.get());
                session.setAttribute("id", null);    //임시로 쓴것이라 지워줌

                cookie = new Cookie("workerId", worker.get().getUserId());
                cookie.setMaxAge(24 * 30 * 60 * 60 * 1000); //30일
                response.addCookie(cookie);
                //로그인에 성공하면 쿠키등록 - 자동로그인

                return "redirect:/worker";
            } else if (member.isPresent()) {
                session.setAttribute("member", member.get());
                session.setAttribute("id", null);    //임시로 쓴것이라 지워줌

                cookie = new Cookie("memberId", member.get().getUserId());
                cookie.setMaxAge(24 * 30 * 60 * 60 * 1000); //30일
                response.addCookie(cookie);
                //로그인에 성공하면 쿠키등록 - 자동로그인

                return "redirect:/member";
            } else {
                //요보사, 보호자에 해당하는 아이디가 없다면 회원가입해야함

                //쿠키가 없는게 아니고 조작되서 로그인을 실패했을 수 있으니 삭제함
                cookie = new Cookie("workerId", null);  // 쿠키 값을 null로 설정
                cookie.setMaxAge(0);  // 남은 만료시간을 0으로 설정
                response.addCookie(cookie);

                cookie = new Cookie("memberId", null);  // 쿠키 값을 null로 설정
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                return "redirect:/index/accountChoice";
            }
        }
        return null;
    }

    //카카오 API
    @GetMapping("/kakao")
    public String kakao(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("snsType","kakao");

        String id = kakaoService.kakaoLoginAPI(request);
        session.setAttribute("id", id);
        log.info("kakao : id = {}", id);

        return "redirect:/index/signIn";
    }

    //네이버 API
    @GetMapping("/naver")
    public String naver(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("snsType","naver");


        String id = naverService.naverLoginAPI(request);
        session.setAttribute("id", id);
        log.info("naver : id = {}");

        return "redirect:/index/signIn";
    }

    //로그아웃, 쿠키 삭제
    @GetMapping("/signOut")
    public String signOut(HttpSession session, HttpServletResponse response) {
        Cookie cookie;

        //자동로그인 해제
        cookie = new Cookie("workerId", null);  // 쿠키 값을 null로 설정
        cookie.setMaxAge(0);  // 남은 만료시간을 0으로 설정
        response.addCookie(cookie);

        cookie = new Cookie("memberId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        session.invalidate();

        return "redirect:/index";
    }

    //주소 선택 뷰
    @GetMapping("/addressChoice")
    public String addressChoice(Model model) {
        log.info("addressChoice");

        List<Address> addressList = addressService.getAddressList();

        Map<String, ArrayList<String>> addressMapList = new HashMap<>();

        for (Address address : addressList) {
            ArrayList<String> strings = addressMapList.get(address.getAddress1());
            if (strings == null) {
                ArrayList<String> tmpList = new ArrayList<>();
                tmpList.add(address.getAddress2());
                addressMapList.put(address.getAddress1(),tmpList);
            }else {
                strings.add(address.getAddress2());
            }
        }
        model.addAttribute("addressList", addressMapList);




        return "front/index/addressChoice";
    }
}
