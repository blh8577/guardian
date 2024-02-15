package com.dependent.guardian.service.worker;

import com.dependent.guardian.entity.address.Address;
import com.dependent.guardian.entity.worker.Worker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface WorkerService {

    Optional<Worker> signIn(String id);

    Worker signUp(Worker worker, HttpServletRequest request) throws ServletException, IOException;

    List<Worker> getWorkerList(List<Integer> workerIdx, Address address);

    Optional<Worker> getWorker(Integer workerIdx);

    Worker update(Worker worker);








}
