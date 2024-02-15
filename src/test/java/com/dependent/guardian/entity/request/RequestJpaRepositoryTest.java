package com.dependent.guardian.entity.request;

import com.dependent.guardian.entity.worker.Worker;
import com.dependent.guardian.entity.worker.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//내장 db안쓰기옵션
class RequestJpaRepositoryTest {

    RequestRepository requestRepository;
    WorkerRepository workerRepository;

    @Autowired
    public RequestJpaRepositoryTest(RequestRepository requestRepository, WorkerRepository workerRepository) {
        this.requestRepository = requestRepository;
        this.workerRepository = workerRepository;
    }

    @Test
    void findById() {
        //given
        Request save = requestRepository.save(getRequest());

        //when
        Optional<Request> request = requestRepository.findById(save.getRequestIdx());

        //then
        assertThat(request).isPresent();
        assertThat(request.get()).isEqualTo(save);

    }

    @Test
    void findByCondition() {
        //given
        List<Request> beforeList = requestRepository.findByCondition("대기중");

        Request save;

        //when
        requestRepository.save(getRequest());
        List<Request> afterList = requestRepository.findByCondition("대기중");

        //then
        assertThat(afterList.size()).isGreaterThan(beforeList.size());

    }
    //수락 상태 List,

    @Test
    void findByWorkerIdxAndCondition() {
        //given
        Request save1 = requestRepository.save(getRequest());
        Request save2 = requestRepository.save(getRequest());
        Request save3 = requestRepository.save(getRequest());

        //when
        List<Request> beforeList = requestRepository.findByWorkerIdxAndCondition(1, "수락");
        save1.setCondition("수락");
        requestRepository.save(save1);
        List<Request> afterList = requestRepository.findByWorkerIdxAndCondition(1, "수락");


        //then
        assertThat(afterList.size()).isGreaterThan(beforeList.size());

    }
    //환자정보List, 요양보호사 매칭여부,

    @Test
    void findByMemberIdxAndCondition() {
        //given
        List<Request> beforeList = requestRepository.findByMemberIdxAndCondition(1, "대기중");
        Request save = getRequest();

        //when
        requestRepository.save(save);
        List<Request> afterList = requestRepository.findByMemberIdxAndCondition(1, "대기중");

        //then
        assertThat(afterList.size()).isEqualTo(beforeList.size() + 1);


    }
    //보호자 매칭여부, 보호자 신청 List,

    @Test
    void findByMemberIdxAndConditionNotIn() {
        //given
        List<String> condition = new ArrayList<>();
        condition.add("수락");
        condition.add("대기중");

        //when
        List<Request> beforeList = requestRepository.findByMemberIdxAndConditionNotIn(1, condition);
        Request save = getRequest();
        save.setCondition("종료");
        requestRepository.save(save);
        List<Request> afterList = requestRepository.findByMemberIdxAndConditionNotIn(1, condition);
        //나중에 본거라 확실치 않지만, afterList에서 꺼낸 workerIdx들이 최종적으로 보호자 매칭List(보호자가 매칭했었던)임


        //then
        assertThat(afterList.size()).isEqualTo(beforeList.size() + 1);
        assertThat(afterList).contains(save);

    }
    //보호자 매칭 List

    @Test
    void findByWorkerIdxAndConditionNotIn() {
        //given
        List<String> condition = new ArrayList<>();
        condition.add("수락");
        condition.add("대기중");

        //when
        List<Request> beforeList = requestRepository.findByWorkerIdxAndConditionNotIn(1, condition);
        Request save = getRequest();
        save.setCondition("종료");
        requestRepository.save(save);
        List<Request> afterList = requestRepository.findByWorkerIdxAndConditionNotIn(1, condition);


        //then
        assertThat(afterList.size()).isEqualTo(beforeList.size() + 1);
        assertThat(afterList).contains(save);

    }
    //요양보호사 매칭 List

    @Test
    void findByWorkerIdxNotIn() {
        //given
        Request save1 = getRequest();
        Request save2 = getRequest();
        Request save3 = getRequest();

        save1.setWorkerIdx(2);
        save3.setWorkerIdx(3);

        requestRepository.save(save1);
        requestRepository.save(save2);
        requestRepository.save(save3);
        //요청 3개를 생성하고 저장. 구분을 위해 workerIdx를 다르게 저장


        workerRepository.save(getWorker());
        Worker worker1 = getWorker();
        Worker worker2 = getWorker();
        Worker worker3 = getWorker();
        worker1.setUserId("ddddd");
        worker2.setUserId("asdfasdf");
        worker3.setUserId("asdfaddsdf");
        workerRepository.save(worker1);
        workerRepository.save(worker2);
        workerRepository.save(worker3);

        save1.setWorkerIdx(worker1.getWorkerIdx());
        save2.setWorkerIdx(worker2.getWorkerIdx());
        save3.setWorkerIdx(worker3.getWorkerIdx());


        //when
        List<Request> conditions = requestRepository.findByCondition("수락");
        if (conditions.size()==0) {
            Request request = new Request();
            request.setWorkerIdx(0);
            conditions.add(request);
        }
        //not in은 null이면 무조건 false이기 때문에 0값을 임의로 삽입해줌

        List<Integer> workerIdxList = conditions.stream().map(Request::getWorkerIdx).collect(Collectors.toList());
        List<Request> beforeNotIn = requestRepository.findByWorkerIdxNotIn(workerIdxList);


        save1.setCondition("수락");
        save2.setCondition("수락");
        save3.setCondition("수락");
        requestRepository.save(save1);
        requestRepository.save(save2);
        requestRepository.save(save3);

        conditions = requestRepository.findByCondition("수락");
        workerIdxList = conditions.stream().map(Request::getWorkerIdx).collect(Collectors.toList());
        List<Request> afterNotIn = requestRepository.findByWorkerIdxNotIn(workerIdxList);


        //then
        assertThat(afterNotIn.size()).isEqualTo(beforeNotIn.size() - 3);
        //수락이 된 개수만큼 after(신청가능 갯수)가 줄어듬
    }
    //요보사 신청 List

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