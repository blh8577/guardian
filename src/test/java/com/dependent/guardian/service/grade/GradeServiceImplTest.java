package com.dependent.guardian.service.grade;

import com.dependent.guardian.entity.grade.Grade;
import com.dependent.guardian.entity.grade.GradeRepository;
import com.dependent.guardian.entity.request.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradeServiceImplTest {

    @InjectMocks
    GradeServiceImpl gradeService;

    @Mock
    GradeRepository gradeRepository;

    @Test
    void saveGrade() {
        //given
        Grade grade = new Grade();
        when(gradeRepository.save(any())).thenAnswer(invocation -> {
            grade.setGradeIdx(1);
            return grade;
        });

        Request request = new Request();

        //when
        gradeService.saveGrade(request,3);

        //then
        assertThat(grade.getGradeIdx()).isNotNull();

    }
}