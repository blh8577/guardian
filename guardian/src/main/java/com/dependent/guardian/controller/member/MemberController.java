package com.dependent.guardian.controller.member;

import com.dependent.guardian.entity.member.Member;
import com.dependent.guardian.entity.request.Request;
import com.dependent.guardian.service.member.MemberService;
import com.dependent.guardian.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final RequestService requestService;

    //보호자 매칭 되었는지 확인
    @GetMapping
    public String checkMatching(HttpSession session){
        Member member = (Member) session.getAttribute("member");
        Request request = requestService.getMatching(member);

        Boolean result = request != null;

        log.info("checkMatching : memberIdx = {}, result = {}",member.getMemberIdx(), result);

        if(result){ //매칭되었다면
            return "redirect:/request/member/matching"; // 보호자의 매칭된 뷰
        }else {
            return "redirect:/index/addressChoice";   //주소 선택 뷰
        }

    }

    //회원가입 뷰
    @GetMapping("/signUp")
    public String signUp() {
        log.info("signUp-Get");

        return "front/member/signUp";
    }

    //회원가입 로직
    @PostMapping("/signUp")
    public String signUp(Member member, Model model, HttpSession session) {
        String scriptMsg;
        String scriptUrl;

        member.setUserId((String) session.getAttribute("id"));
        member.setSnsType(session.getAttribute("snsType").toString());
        //유저 id 삽입

        log.info("signUp-Post");

        Member save = memberService.signUp(member);
        if (save.getMemberIdx() == null) {
            log.error("signUp-Post : memberIdx is empty");

            scriptMsg = "가입 실패\n관리자에게 문의";
            scriptUrl = "/index";
        }else{
            log.info(("signUp-Post : signUp Success"));

            scriptMsg = save.getName()+"님 가입을 환영합니다.";
            scriptUrl = "/index/signIn";
        }
        model.addAttribute("scriptMsg", scriptMsg);
        model.addAttribute("scriptMsg", scriptUrl);

        return "onlyScript";
    }

    //보호자 마이페이지
    //프론트는 session에 있는 member사용
    @GetMapping("/myprofile")
    public String myprofile() {
        return "front/member/myprofile";
    }


}
