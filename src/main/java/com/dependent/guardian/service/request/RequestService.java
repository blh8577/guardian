package com.dependent.guardian.service.request;

import com.dependent.guardian.entity.request.Request;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    Optional<Request> getRequest(Integer requestIdx);

    String changeCondition(Integer requestIdx, String condition);

    Request saveRequest(Request request);

    Request updateReview(Integer requestIdx, String review);

    Request getMatching(Object object);

    List<Request> getRequestList(Integer memberIdx);

    List<Request> getPastRequestList(Integer memberIdx);
    //보호자의 과거신청 목록

    List<Integer> getImPossibleWorkerList();
    //신청이 가능한 요보사들을 출력해주지만, 주소는 Worker에서 걸러줘야함 - Worker에서 사용

    List<Request> getWaitingList(Integer workerIdx);
    //요보사의 신청 받은 정보

    List<Request> getWorkerReview(Integer workerIdx);
    //요보사 리뷰 가져오기

    List<Request> getPastMatchingList(Integer workerIdx);
    //요보사의 과거 매칭목록


}
