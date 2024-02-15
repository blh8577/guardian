package com.dependent.guardian.entity.career;

import java.util.List;
import java.util.Optional;

public interface CareerRepository {

    //경력 인덱스로 찾기
    Optional<Career> findById(Integer careerIdx);

    //요보사의 경력들 찾기
    List<Career> findByWorkerIdx(Integer workerIdx);

    //경력 지우기
    void deleteById(Integer id);

    //경력 삽입
    Career save(Career career);




}
