package com.dependent.guardian.controller.api;


import com.dependent.guardian.entity.alarm.Alarm;
import com.dependent.guardian.entity.career.Career;
import com.dependent.guardian.entity.member.Member;
import com.dependent.guardian.entity.request.Request;
import com.dependent.guardian.entity.worker.Worker;
import com.dependent.guardian.service.alarm.AlarmService;
import com.dependent.guardian.service.career.CareerService;
import com.dependent.guardian.service.emotion.EmotionService;
import com.dependent.guardian.service.function.KakaoService;
import com.dependent.guardian.service.function.NaverService;
import com.dependent.guardian.service.request.RequestService;
import com.dependent.guardian.service.worker.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final WorkerService workerService;
    private final EmotionService emotionService;
    private final AlarmService alarmService;
    private final RequestService requestService;
    private final CareerService careerService;
    private final KakaoService kakaoService;

    @GetMapping("/test")
    public String test() {
        return "ㅋㅋㅋㅋㅋㅋ이 화면이 뜨면 성공인데 뜰거같냐? 병신 ㅋㅋㅋㅋㅋ 에헤라이디야 테스트";
    }

    //표정 알림 읽을거 있는지
    @PostMapping("/emotion")
    public Boolean readEmotion(HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            log.error("readEmotion : member is Null");
            return false;
        }
        Request matching = requestService.getMatching(member);
        if (matching == null) {
            log.error("readEmotion : matching is Null");
            return false;
        }
        Boolean haveEmotion = emotionService.isHaveEmotion(matching.getRequestIdx());
        if (haveEmotion == null) {
            log.error("readEmotion : haveEmotion is Null");
            return false;
        }

        log.info("readEmotion : memberIdx = {}, requestIdx = {}, result = {}", member.getMemberIdx(), matching.getRequestIdx(), haveEmotion);

        return haveEmotion;
    }

    //긴급 알람 삽입
    @PostMapping("/alarm")
    public Boolean setAlarm(HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            log.error("setAlarm : member is Null");
            return false;
        }

        Request matching = requestService.getMatching(member);
        if (matching == null) {
            log.error("setAlarm : matching is Null");
            return false;
        }

        Alarm alarm = new Alarm();
        alarm.setRead(0);
        alarm.setRequestIdx(matching.getRequestIdx());
        Boolean result = alarmService.saveAlarm(alarm);
        if (result == null) {
            log.error("setAlarm : result is Null");
            return false;
        }

        log.info("setAlarm : memberIdx = {}, requestIdx = {}, result = {}", member.getMemberIdx(), matching.getRequestIdx(), result);
        return result;
    }



    //경력 업데이트
    @GetMapping("/saveCareer")
    public Boolean saveCareer(Career career, HttpSession session) {
        log.info("들어오긴함?");
        Worker worker = (Worker) session.getAttribute("worker");
        log.info("worker:{}", worker.getWorkerIdx());
        career.setWorkerIdx(worker.getWorkerIdx());
        Boolean result = careerService.saveCareer(career);

        if (result) {
            log.info("saveCareer : workerIdx = {}, result = {}", worker.getWorkerIdx(), result);
        } else {
            log.error("saveCareer : workerIdx = {}, result = {}", worker.getWorkerIdx(), result);
        }
        return result;

    }

    //경력 삭제
    @PostMapping("/deleteCareer")
    public Boolean deleteCareer(Integer careerIdx) {

        Boolean result = careerService.deleteCareer(careerIdx);

        if (result) {
            log.info("saveCareer : careerIdx = {}, result = {}", careerIdx, result);
        } else {
            log.error("saveCareer : careerIdx = {}, result = {}", careerIdx, result);
        }
        return result;

    }

    @GetMapping("/kakaoTest")
    public String naverTest(HttpServletRequest request) throws IOException {
        String loginAPI = kakaoService.kakaoLoginAPI(request);
        return loginAPI;

    }
}
