package com.dependent.guardian.entity.career;

import com.dependent.guardian.entity.worker.Worker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CareerJpaRepositoryTest {

    CareerRepository careerRepository;

    @Autowired
    public CareerJpaRepositoryTest(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    @Test
    void findByList(){
        //given
        int beforeSize = careerRepository.findByWorkerIdx(1).size();
        careerRepository.save(getCareer(1));

        //when
        int afterSize = careerRepository.findByWorkerIdx(1).size();

        //then
        assertThat(beforeSize).isEqualTo(afterSize - 1);


    }

    @Test
    void delete(){
        //given
        Career career = careerRepository.save(getCareer(1));

        //when
        careerRepository.deleteById(career.getCareerIdx());
        Optional<Career> afterCareer = careerRepository.findById(career.getCareerIdx());

        //then
        assertThat(afterCareer).isEmpty();


    }

    Career getCareer(Integer workerIdx){
        Career career = new Career();
        career.setCareer("굳건한 대한민국인입니다.");
        career.setWorkerIdx(workerIdx);

        return career;
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