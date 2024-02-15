package com.dependent.guardian.entity.worker;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest    //jpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//내장 db 안쓰기옵션
class WorkerJpaRepositoryTest {

    WorkerRepository workerRepository;

    @Autowired
    public WorkerJpaRepositoryTest(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }


    @Test
    void findById() {
        //given
        Worker save = workerRepository.save(getWorker());

        //when
        Optional<Worker> worker = workerRepository.findById(save.getWorkerIdx());

        //then
        assertThat(worker.isPresent()).isTrue();
        assertThat(worker.get()).isEqualTo(save);



    }

    @Test
    void findByUserId() {
        //given
        Worker save = workerRepository.save(getWorker());

        //when
        Optional<Worker> worker = workerRepository.findByUserId(save.getUserId());

        //then
        assertThat(worker.isPresent()).isTrue();
        assertThat(worker.get()).isEqualTo(save);

    }

//    @Test
//    void findByWorkerIdxInAndAddress1AndAddress2(){
//        Worker worker1 = getWorker();
//        Worker worker2 = getWorker();
//        Worker worker3 = getWorker();
//
//        worker1.setUserId("a");
//        worker1.setAddress1("서울");
//        worker1.setAddress2("구로");
//        worker2.setUserId("dddddddddd");
//        worker2.setAddress1("서울");
//        worker2.setAddress2("구로");
//        worker3.setAddress1("광주");
//        worker3.setAddress2("구로");
//
//        List<Integer> workerIdxList = new ArrayList<>();
//        workerIdxList.add(workerRepository.save(worker1).getWorkerIdx());
//        workerIdxList.add(workerRepository.save(worker2).getWorkerIdx());
//        workerRepository.save(worker1);
//
//
//
//        List<Worker> result = workerRepository.findByWorkerIdxNotInAndAddress1AndAddress2(workerIdxList, "서울", "구로");
//
//        assertThat(result.size()).isEqualTo(2);
//
//    }
//    //신청가능한 목록중 주소까지 해당하는 목록-코드가 바껴서.. 테스트는 주석처리함

    Worker getWorker(){
        Worker worker = new Worker();

        worker.setUserId("dddd");
        worker.setGrade(3.3f);
        worker.setCamera("dd");
        worker.setName("dd");
        worker.setAddress1("11");
        worker.setLicense("dd");
        worker.setIntroduce("Dd");
        worker.setSex("dd1");
        worker.setPhone("df");
        worker.setSelfie("asdf");
        worker.setAddress2("df");
        worker.setSnsType("df");

        return worker;
    }
}