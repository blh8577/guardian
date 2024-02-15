package com.dependent.guardian.service.request;

import com.dependent.guardian.entity.member.Member;
import com.dependent.guardian.entity.request.Request;
import com.dependent.guardian.entity.request.RequestRepository;
import com.dependent.guardian.entity.worker.Worker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    RequestRepository requestRepository;

    @InjectMocks
    RequestServiceImpl requestService;

    @Test
    void changeCondition() {

        //given
        //수락으로 바뀌어야함
        Request request1 = getRequest();
        request1.setRequestIdx(1);

        //취소로 바뀌어야함
        Request request2 = getRequest();
        request2.setRequestIdx(2);
        request2.setWorkerIdx(2);

        //거절로 바뀌어야함
        Request request3 = getRequest();
        request3.setRequestIdx(3);
        request3.setMemberIdx(2);

        //상태유지되어야함
        Request request4 = getRequest();
        request4.setRequestIdx(4);
        request4.setMemberIdx(3);
        request4.setWorkerIdx(3);

        when(requestRepository.findById(request1.getRequestIdx())).thenReturn(Optional.of(request1));

        //해당 요청에 포함된 요보사에게 신청중인 요청들
        List<Request> requestList = new ArrayList<>();
        requestList.add(request3);
        when(requestRepository.findByWorkerIdxAndCondition(request1.getWorkerIdx(), "대기중")).thenReturn(requestList);

        //해당 요청에 포함된 멤버가 신청중인 요청들
        List<Request> waitingList = new ArrayList<>();
        waitingList.add(request2);
        when(requestRepository.findByMemberIdxAndCondition(request1.getWorkerIdx(), "대기중")).thenReturn(waitingList);


        //when
        requestService.changeCondition(request1.getRequestIdx(), "수락");

        //then
        assertThat(request1.getCondition()).isEqualTo("수락");
        assertThat(request2.getCondition()).isEqualTo("취소");
        assertThat(request3.getCondition()).isEqualTo("거절");
        assertThat(request4.getCondition()).isEqualTo("대기중");


    }

    @Test
    void saveRequest() {
        //given
        Request request = getRequest();
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        //when
        requestService.saveRequest(request);

        //then
        assertThat(request.getDate()).isNotNull();

    }

    @Test
    void updateReview() {
        //given
        Request request = getRequest();
        request.setRequestIdx(1);
        request.setReview(null);
        when(requestRepository.findById(request.getRequestIdx())).thenReturn(Optional.of(request));

        //when
        requestService.updateReview(request.getRequestIdx(),"아 뭐..그랬어요");

        //then
        assertThat(request.getReview()).isNotNull();


    }

    @Test
    void checkMatching() {
        //given
        Worker worker = new Worker();
        Member member = new Member();
        List<Request> requestSize1 = new ArrayList<>();
        List<Request> requestSize0 = new ArrayList<>();
        requestSize1.add(new Request());
        when(requestRepository.findByMemberIdxAndCondition(any(), eq("수락"))).thenReturn(requestSize1);
        when(requestRepository.findByWorkerIdxAndCondition(any(), eq("수락"))).thenReturn(requestSize0);

        //when
        Request memberMatching = requestService.getMatching(member);
        Request workerMatching = requestService.getMatching(worker);

        //then
        assertThat(memberMatching).isNotNull();
        assertThat(workerMatching).isNull();
    }


    //실수로 JPA테스트에서 해버렸음
    @Test
    void getPossibleWorkerList() {
    }


    Request getRequest() {
        Request request = new Request();

        request.setGrade(3.0f);
        request.setAge("100");
        request.setDate(LocalDateTime.now());
        request.setDisease("아픔");
        request.setDetail("많이 아파요");
        request.setHospital("성심병원");
        request.setMemberIdx(1);
        request.setName("배량현");
        request.setSex("여");
        request.setWorkerIdx(1);
        request.setCondition("대기중");

        return request;
    }
}