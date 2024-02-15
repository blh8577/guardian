package com.dependent.guardian.entity.worker;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository {

    Optional<Worker> findById(Integer workerIdx);

    Optional<Worker> findByUserId(String userId);

    Worker save(Worker worker);

    List<Worker> findByWorkerIdxIn(List<Integer> workerIdx);
    //이거..안쓰나..??????

    //신청 가능한 요보사들 출력
    List<Worker> findByWorkerIdxNotInAndAddress1AndAddress2(List<Integer> workerIdx, String address1, String address2);





}
