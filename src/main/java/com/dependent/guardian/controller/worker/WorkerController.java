package com.dependent.guardian.controller.worker;

import com.dependent.guardian.entity.address.Address;
import com.dependent.guardian.entity.career.Career;
import com.dependent.guardian.entity.request.Request;
import com.dependent.guardian.entity.worker.Worker;
import com.dependent.guardian.service.career.CareerService;
import com.dependent.guardian.service.request.RequestService;
import com.dependent.guardian.service.worker.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/worker")
public class WorkerController {
    private final WorkerService workerService;
    private final RequestService requestService;
    private final CareerService careerService;

    //요보사 매칭 되었는지 확인
    @GetMapping
    public String checkMatching(HttpSession session) {
        Worker worker = (Worker) session.getAttribute("worker");
        Request request = requestService.getMatching(worker);
        Boolean result = request !=null ;
        log.info("checkMatching : memberIdx = {}, result = {}", worker.getWorkerIdx(), result);

        if (result) { //매칭되었다면
            return "redirect:/request/worker/matching"; // 보호자의 매칭된 뷰
        } else {
            return "redirect:/request/worker/waitingList";   //주소 선택 뷰
        }
    }

    //회원가입 뷰
    @GetMapping("/signUp")
    public String signUp() {
        log.info("signUp-Get");

        return "front/worker/signUp";
    }

    //회원가입 로직
    @PostMapping("/signUp")
    public String signUp(Worker worker, Model model, HttpServletRequest request) throws ServletException, IOException {
        String scriptMsg;
        String scriptUrl;
        HttpSession session = request.getSession();

        worker.setUserId((String) session.getAttribute("id"));
        worker.setSnsType(session.getAttribute("snsType").toString());
        //유저 id 삽입

        log.info("signUp-Post");

        Worker save = workerService.signUp(worker, request);
        if (save.getWorkerIdx() == null) {
            log.error("signUp-Post : workerIdx is empty");

            scriptMsg = "가입 실패\n관리자에게 문의";
            scriptUrl = "/index";
        } else {
            log.info(("signUp-Post : signUp Success"));

            scriptMsg = save.getName() + "님 가입을 환영합니다.";
            scriptUrl = "/index/signIn";
        }
        model.addAttribute("scriptMsg", scriptMsg);
        model.addAttribute("scriptUrl", scriptUrl);

        return "onlyScript";
    }

    //마이프로필
    @GetMapping("/myprofile")
    public String myprofile(Model model,HttpSession session) {
        Worker worker = (Worker) session.getAttribute("worker");
        List<Career> careerList = careerService.getCareerList(worker.getWorkerIdx());
        model.addAttribute("careerList", careerList);
        log.info("myprofile");

        return "front/worker/myprofile";
    }

    //신청가능한 요보사 목록
    @GetMapping("/workerList")
    public String workerList(Address address, Model model) {
        log.info("workerList");

        List<Integer> possibleWorkerList = requestService.getImPossibleWorkerList();

        List<Worker> workerList = workerService.getWorkerList(possibleWorkerList, address);

        log.info("possibleWorkerList.toString() = {}",possibleWorkerList.toString());

        log.info("workerList.toString() = {}",workerList.toString());
        model.addAttribute("workerList",workerList);

        return "front/worker/workerList";

    }

    //요보사 디테일
    @GetMapping("/workerDetail")
    public String workerDetail(Integer workerIdx, Model model) {
        log.info("workerDetail");

        Optional<Worker> worker = workerService.getWorker(workerIdx);
        if (worker.isPresent()) {
            List<Request> workerReview = requestService.getWorkerReview(workerIdx);
            model.addAttribute("worker", worker.get());
            model.addAttribute("requestList", workerReview);
            model.addAttribute("careerList",careerService.getCareerList(workerIdx));

            return "front/worker/workerDetail";
        } else {
            log.error("workerDetail : worker is Null");
            String scriptMsg = "요양 보호사의 정보를 찾지 못하였습니다.";
            String scriptUrl = "/index";
            model.addAttribute("scriptMsg", scriptMsg);
            model.addAttribute("scriptUrl", scriptUrl);
            return "onlyScript";
        }
    }

    //개인정보 업데이트
    @PostMapping("/update")
    public String update(Worker worker, Model model, HttpSession session) {
        Worker sessionWorker = (Worker) session.getAttribute("worker");
        worker.setWorkerIdx(sessionWorker.getWorkerIdx());
        Worker update = workerService.update(worker);
        String scriptMsg;
        String scriptUrl;

        if (update == null) {
            log.error("introduceUpdate : workerIdx = {}, result = {}", worker.getWorkerIdx(), null);
            scriptMsg = "개인정보 수정에 실패하였습니다.";
            scriptUrl = "/worker/myprofile";
        } else {
            log.info("introduceUpdate : workerIdx = {}, result = {}", worker.getWorkerIdx(), true);
            scriptMsg = "개인정보 수정에 성공하였습니다.";
            scriptUrl = "/index";
            //업데이트된 정보로 다시 받아오기위해 다시 로그인
        }

        model.addAttribute("scriptMsg", scriptMsg);
        model.addAttribute("scriptUrl", scriptUrl);
        return "onlyScript";

    }


    //실시간 카메라
    /*미완성*/
    @GetMapping("/camera")
    public String camera() {

        return "redirect:/index";

    }
}
