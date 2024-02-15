package com.dependent.guardian.service.career;

import com.dependent.guardian.entity.career.Career;
import com.dependent.guardian.entity.career.CareerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CareerServiceImplTest {

    @InjectMocks
    CareerServiceImpl careerService;

    @Mock
    CareerRepository careerRepository;


    @Test
    void getCareerList() {
        //given
        List<Career> careerList = new ArrayList<>();

        Career career1 = new Career();
        careerList.add(career1);

        Career career2 = new Career();
        careerList.add(career2);

        when(careerRepository.findByWorkerIdx(any())).thenReturn(careerList);


        //when
        List<Career> list = careerService.getCareerList(1);


        //then
        assertThat(list.size()).isEqualTo(2);
        assertThat(list).isEqualTo(careerList);


    }

    @Test
    void deleteCareer() {
        //given
        Career career2 = new Career();

        Optional<Career> nullCareer = Optional.empty();
        Optional<Career> optionalCareer = Optional.of(career2);

        when(careerRepository.findById(1)).thenReturn(nullCareer);
        //1은 지웠다고 가정
        when(careerRepository.findById(2)).thenReturn(optionalCareer);
        //2는 안지웠다고 가정

        //when
        Boolean trueResult = careerService.deleteCareer(1);
        Boolean falseResult = careerService.deleteCareer(2);

        //then
        assertThat(trueResult).isTrue();
        assertThat(falseResult).isFalse();
    }

    @Test
    void saveCareer() {
        //given
        Career career = new Career();

        when(careerRepository.save(career)).thenAnswer(invocation -> {
           career.setCareerIdx(1);
           return career;
        });

        //when
        Boolean result = careerService.saveCareer(career);

        //then
        assertThat(result).isTrue();

    }
}