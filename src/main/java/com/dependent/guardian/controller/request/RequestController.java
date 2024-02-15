package com.dependent.guardian.controller.request;

import com.dependent.guardian.entity.grade.Grade;
import com.dependent.guardian.entity.member.Member;
import com.dependent.guardian.entity.record.Record;
import com.dependent.guardian.entity.request.Request;
import com.dependent.guardian.entity.worker.Worker;
import com.dependent.guardian.service.grade.GradeService;
import com.dependent.guardian.service.record.RecordService;
import com.dependent.guardian.service.request.RequestService;
import com.dependent.guardian.service.worker.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private final WorkerService workerService;
    private final GradeService gradeService;
    private final RecordService recordService;


    //보호자 현재 매칭 정보 뷰
    @GetMapping("/member/matching")
    public String memberMatching(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");

        log.info("memberMatching : memberIdx = {}", member.getMemberIdx());

        //매칭 정보
        Request matching = requestService.getMatching(member);

        if (matching != null) {
            model.addAttribute("request", matching);

            //요보사 정보
            Optional<Worker> worker = workerService.getWorker(matching.getWorkerIdx());
            if (worker.isPresent()) {
                model.addAttribute("worker", worker.get());
            } else {
                log.error("memberMatching : worker is Null");
                return "redirect:/index";
            }

            //평점 입력해야하는 시간이 되었는지
            Boolean recentGrade = gradeService.getRecentGrade(matching);
            model.addAttribute("recentGrade", recentGrade);

            //녹화영상들
            List<Record> recordList = recordService.getRecordList(matching.getRequestIdx());
            model.addAttribute("recordList", recordList);

            return "front/request/member/matching";
        } else {
            log.error("memberMatching : matching is Null");
            return "redirect:/index";
        }
    }

    //보호자 평점 삽입 뷰
    @GetMapping("/member/saveGrade")
    public String memberSaveGrade() {
        log.info("memberSaveGrade-Get");

        return "front/request/member/saveGrade";

    }

    //보호자 평점 삽입 로직
    @PostMapping("/member/saveGrade")
    public String memberSaveGrade(HttpSession session, Integer point) {
        Member member = (Member) session.getAttribute("member");
        Request matching = requestService.getMatching(member);

        log.info("memberSaveGrade-Post : memberIdx = {}, matchingIdx = {}", member.getMemberIdx(), matching.getRequestIdx());

        Grade grade = gradeService.saveGrade(matching, point);

        log.info("memberSaveGrade-Post : grade = {}", grade);

        return "redirect:/request/member/matching";


    }

    //보호자 과거 매칭 목록 뷰
    @GetMapping("/member/pastRequestList")
    public String memberPastRequestList(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        log.info("memberMatching");

        List<Request> pastRequestList = requestService.getPastRequestList(member.getMemberIdx());
        model.addAttribute("requestList", pastRequestList);

        return "front/request/member/pastRequestList";
    }

    //보호자 과거 매칭 디테일 뷰
    @GetMapping("/member/pastRequestDetail")
    public String memberPastRequestDetail(Integer requestIdx, Model model) {

        //과거 요청 디테일
        Optional<Request> request = requestService.getRequest(requestIdx);

        if (request.isPresent()) {
            model.addAttribute("request", request.get());

            //요보사 정보
            Optional<Worker> worker = workerService.getWorker(request.get().getWorkerIdx());
            if (worker.isPresent()) {
                model.addAttribute("worker", worker.get());
            } else {
                log.error("memberPastRequestDetail : worker is Null");
                return "redirect:/index";
            }

            //녹화영상들
            List<Record> recordList = recordService.getRecordList(request.get().getRequestIdx());
            model.addAttribute("recordList", recordList);
            return "front/request/member/pastRequestDetail";
        } else {
            log.error("memberPastRequestDetail : request is Null");
            return "redirect:/index";
        }
    }


    //보호자 매칭 종료 뷰
    @GetMapping("/member/endOfMatching")
    public String memberEndOfMatching() {
        log.info("memberEndOfMatching-Get");

        return "front/request/member/endOfMatching";

    }

    //보호자 매칭 종료 로직
    @PostMapping("/member/endOfMatching")
    public String memberEndOfMatching(String review, Integer point, HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        Request matching = requestService.getMatching(member);
        String scriptMsg;
        String scriptUrl;

        log.info("memberEndOfMatching-Post : memberIdx = {}, matchingIdx = {}", member.getMemberIdx(), matching.getRequestIdx());

        Request request = requestService.updateReview(matching.getRequestIdx(), review);
        if (request == null) {
            log.error("memberEndOfMatching-Post : request is Null");
            scriptMsg = "매칭 종료를 실패 하였습니다.";
            scriptUrl = "/member";
        } else {
            gradeService.saveGrade(matching, point);
            scriptMsg = requestService.changeCondition(matching.getRequestIdx(), "종료");
            scriptUrl = "/member";
        }
        model.addAttribute("scriptMsg", scriptMsg);
        model.addAttribute("scriptUrl", scriptUrl);

        return "onlyScript";
    }

    //보호자 요청 생성 뷰
    @GetMapping("/member/saveRequest")
    public String memberSaveRequest(Worker worker, HttpSession session, Model model) {
        Request matching = requestService.getMatching(worker);

        log.info("memberSaveRequest : workerIdx = {}, matching is null = {}", worker.getWorkerIdx(), matching == null);

        if (matching != null) {
            String scriptMsg = "해당 요양보호사는 이미 매칭중인 상태입니다.";
            String scriptUrl = "/index/addressChoice";
            model.addAttribute("scriptMsg", scriptMsg);
            model.addAttribute("scriptUrl", scriptUrl);
            return "onlyScript";
        } else {
            model.addAttribute("workerIdx", worker.getWorkerIdx());
            return "front/request/member/saveRequest";
        }
    }

    //보호자 요청 생성 로직
    @PostMapping("/member/saveRequest")
    public String memberSaveRequest(Request request, HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        request.setMemberIdx(member.getMemberIdx());
        log.info("memberSaveRequest-Post : workerIdx = {}", request.getWorkerIdx());

        Request saveRequest = requestService.saveRequest(request);
        if (saveRequest.getRequestIdx() == null) {
            log.error("memberSaveRequest-Post : requestIdx is Null");
            return "redirect:/index/addressChoice";
        } else {
            String scriptMsg = "성공적으로 매칭을 신청하였습니다.";
            String scriptUrl = "/request/member/requestList";
            model.addAttribute("scriptMsg", scriptMsg);
            model.addAttribute("scriptUrl", scriptUrl);
            return "onlyScript";
        }
    }

    //보호자 요청중인 목록
    @GetMapping("/member/requestList")
    public String memberRequestList(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");

        log.info("memberRequestList");

        List<Request> requestList = requestService.getRequestList(member.getMemberIdx());

        if (requestList.isEmpty()) {
            String scriptMsg = "현재 신청중인 요청이 없습니다.\n요청을 신청한 후에 방문해 주세요.";
            String scriptUrl = "/index/addressChoice";
            model.addAttribute("scriptMsg", scriptMsg);
            model.addAttribute("scriptUrl", scriptUrl);
            return "onlyScript";
        } else {
            model.addAttribute("requestList", requestList);
            return "front/request/member/requestList";
        }
    }

    //보호자의 신청 취소
    @GetMapping("/member/cancelRequest")
    public String memberCancelRequest(Integer requestIdx, Model model) {
        log.info("memberCancelRequest : requestIdx = {}", requestIdx);

        String scriptMsg = requestService.changeCondition(requestIdx, "취소");
        String scriptUrl = "/request/member/requestList";
        model.addAttribute("scriptMsg", scriptMsg);
        model.addAttribute("scriptUrl", scriptUrl);

        return "onlyScript";
    }

    //요보사의 신청 취소
    @GetMapping("/worker/deny")
    public String workerDeny(Integer requestIdx, Model model) {
        log.info("workerDeny : requestIdx = {}", requestIdx);

        String scriptMsg = requestService.changeCondition(requestIdx, "거절");
        String scriptUrl = "/request/worker/waitingList";
        model.addAttribute("scriptMsg", scriptMsg);
        model.addAttribute("scriptUrl", scriptUrl);

        return "onlyScript";
    }

    //요보사 현재 매칭 정보 뷰
    @GetMapping("/worker/matching")
    public String workerMatching(HttpSession session, Model model) {
        Worker worker = (Worker) session.getAttribute("worker");

        log.info("workerMatching : workerIdx = {}", worker.getWorkerIdx());

        Request matching = requestService.getMatching(worker);

        if (matching != null) {
            model.addAttribute("request", matching);
            return "front/request/worker/matching";
        } else {
            log.error("workerMatching : matching is Null");
            return "redirect:/index";
        }
    }

    //요보사 요청 받은 목록
    @GetMapping("/worker/waitingList")
    public String workerWaitingList(HttpSession session, Model model) {
        Worker worker = (Worker) session.getAttribute("worker");
        List<Request> waitingList = requestService.getWaitingList(worker.getWorkerIdx());

        log.info("workerWaitingList : workerIdx = {}", worker.getWorkerIdx());

        model.addAttribute("requestList", waitingList);

        return "front/request/worker/waitingList";
    }

    //요보자 매칭 취소 로직
    @PostMapping("/worker/endOfMatching")
    public String workerEndOfMatching(HttpSession session, Model model) {
        Worker worker = (Worker) session.getAttribute("worker");

        log.info("workerEndOfMatching - Post");

        /*요보사가 취소시 패널티를 줄것인지 고민해볼것 - 지금 로직은 패널티 없음*/
        Request matching = requestService.getMatching(worker);

        String scriptMsg = requestService.changeCondition(matching.getRequestIdx(), "종료");
        String scriptUrl = "/worker";

        model.addAttribute("scriptMsg", scriptMsg);
        model.addAttribute("scriptUrl", scriptUrl);

        return "onlyScript";
    }


    //요보사 과거 매칭 이력
    @GetMapping("/worker/pastMatchingList")
    public String workerPastMatchingList(HttpSession session, Model model) {
        Worker worker = (Worker) session.getAttribute("worker");

        log.info("workerPastMatchingList");

        List<Request> pastMatchingList = requestService.getPastMatchingList(worker.getWorkerIdx());

        model.addAttribute("requestList", pastMatchingList);

        return "front/request/worker/pastMatchingList";
    }

    //신청 받은 요청 디테일
    @GetMapping("/worker/requestDetail")
    public String workerRequestDetail(Integer requestIdx, Model model) {
        log.info("workerRequestDetail");

        Optional<Request> request = requestService.getRequest(requestIdx);
        if (request.isPresent()) {
            model.addAttribute("request", request.get());
            return "front/request/worker/requestDetail";
        } else {
            log.error("workerRequestDetail : request is Null");
            return "redirect:/request/worker/waitingList";
        }

    }

    //요보사의 요청 수락
    @GetMapping("/worker/accept")
    public String workerAccept(Integer requestIdx, Model model) {
        log.info("workerAccept : requestIdx = {}", requestIdx);

        String scriptMsg = requestService.changeCondition(requestIdx, "수락");
        String scriptUrl = "/worker";

        model.addAttribute("scriptMsg", scriptMsg);
        model.addAttribute("scriptUrl", scriptUrl);

        return "onlyScript";
    }

}
