package com.dependent.guardian.entity.grade;

import java.util.List;

public interface GradeRepository {

    //평점 삽입
    Grade save(Grade grade);

    List<Grade> findByRequestIdxOrderByDateDesc(Integer requestIdx);

    //? 안쓰는건가?
    /* 테스트 안해봄*/
    List<Grade> findByRequestIdx(Integer requestIdx);

}
