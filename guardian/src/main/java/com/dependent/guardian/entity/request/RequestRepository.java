package com.dependent.guardian.entity.request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository {


    //요청 인덱스로 찾기
    Optional<Request> findById(Integer requestIdx);

    //상태에 해당되는 요청들
    List<Request> findByCondition(String condition);
    //수락 상태 List,

    //요청 삽입
    Request save(Request request);
    //리뷰업데이트, 날짜 업데이트, 요청생성

    //요보사와 상태로 요청들 찾기
    List<Request> findByWorkerIdxAndCondition(Integer workerIdx, String condition);
    //환자정보List, 요양보호사 매칭여부, 요보사 리뷰조회

    //보호자와 상태로 요청들 찾기
    List<Request> findByMemberIdxAndCondition(Integer memberIdx, String condition);
    //보호자 매칭여부, 보호자 신청 List,

    List<Request> findByMemberIdxAndConditionNotIn(Integer memberIdx, List<String> condition);
    //보호자 매칭 List

    List<Request> findByWorkerIdxAndConditionNotIn(Integer workerIdx, List<String> condition);
    //요양보호사 매칭

    List<Request> findByWorkerIdxNotIn(List<Integer> workerIdx);
    //요보사 신청 List






}
