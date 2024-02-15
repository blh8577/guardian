package com.dependent.guardian.entity.grade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest    //jpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//내장 db안쓰기옵션
class GradeJpaRepositoryTest {

    GradeRepository gradeRepository;

    @Autowired
    public GradeJpaRepositoryTest(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Test
    void save(){
        //given
        Grade grade = new Grade();
        grade.setDate(LocalDateTime.now());
        grade.setPoint(5);
        grade.setRequestIdx(1);
        grade.setWorkerIdx(1);

        //when
        Grade grade1 = gradeRepository.save(grade);

        //then
        assertThat(grade).isEqualTo(grade1);

    }



}