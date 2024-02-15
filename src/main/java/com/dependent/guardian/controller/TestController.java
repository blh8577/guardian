package com.dependent.guardian.controller;

import com.dependent.guardian.entity.member.Member;
import com.dependent.guardian.entity.worker.Worker;
import com.dependent.guardian.service.function.KakaoService;
import com.dependent.guardian.service.function.NaverService;
import com.dependent.guardian.service.worker.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@SessionAttributes("member")
@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final WorkerService workerService;


    @GetMapping("/script")
    public String script(Model model, HttpServletRequest request) throws IOException {
        System.out.println("request.getContextPath() = " + request.getContextPath());
        System.out.println("request.getMethod() = " + request.getMethod());
        System.out.println("request.getQueryString() = " + request.getQueryString());
        System.out.println("request.getServletPath() = " + request.getServletPath());
        System.out.println("request.getRequestURI() = " + request.getRequestURI());


        model.addAttribute("scriptMsg", "ㅋㅋㅋㅋㅋ성공");
        model.addAttribute("scriptUrl", "/index");

        return "onlyScript";
    }


    @GetMapping("/writeSession")
    public String writeTmp(Model model) {
        model.addAttribute(new Member());

        return null;
    }

    @GetMapping("/printSession")
    public String printTmp(@SessionAttribute(value = "member", required = false) Member member) {
        System.out.println("member.getUserId() = " + member.getUserId());

        return null;
    }

    @GetMapping("/deleteSession")
    public String delete(@SessionAttribute(value = "member") Member member, SessionStatus sessionStatus,Model model) {

        sessionStatus.setComplete();
        member = new Member();
        member.setUserId("바뀜");
        model.addAttribute(member);

        return null;
    }

    @GetMapping("/updateSession")
    public String update(@ModelAttribute(value = "member") Member member) {

        member.setUserId("ddddlfjdlfjldlfjldjfajklsdjfgl;sjgoljgoljgljfasdkl");
        return null;
    }

    /*
    * 등록시엔 Model add 해야하고
    * 삭제시엔 SessionStatus.setComplete
    * 업데이트 SesseionAttribute,ModelAttribute Model도 됨
    *
    * */



    @GetMapping("/test")
    public String test(HttpServletRequest request){
//        System.out.println(request.getServletContext().getRealPath("/"));

        return "test";
    }

    //이미지 업로드 테스트
    @PostMapping("/test")
    public String testPost(HttpServletRequest request, RedirectAttributes redirectAttributes) throws ServletException, IOException {
        Worker worker = new Worker();
        worker.setIntroduce("dfdfdf");
        worker.setUserId("dfdfasdhwrh");
        worker.setPhone("Asdfasdf");
        worker.setSex("a");
        worker.setName("asdf");
        worker.setSnsType("kak");
        worker.setAddress2("Asdf");
        worker.setAddress1("asdf");
        worker.setCamera("ASdf");

        System.out.println("오긴함?");

        worker = workerService.signUp(worker, request);
        System.out.println(worker);
        redirectAttributes.addAttribute("ddddd", worker.getSelfie());


        return "redirect:/test/test";
    }



    @GetMapping("/login")
    public String login(){
        return "testLogin";
    }

}
