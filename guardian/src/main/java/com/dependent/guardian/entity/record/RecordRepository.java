package com.dependent.guardian.entity.record;

import java.util.List;

public interface RecordRepository {

    //요청이 가지고 있는 녹화영상들을 시간 역순으로 출력
    List<Record> findByRequestIdxOrderByDateDesc(Integer requestIdx);
}
