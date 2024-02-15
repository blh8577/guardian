package com.dependent.guardian.entity.alarm;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest    //jpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//내장 db안쓰기옵션
class AlarmJpaRepositoryTest {

    AlarmRepository alarmRepository;

    @Autowired
    public AlarmJpaRepositoryTest(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Test
    void save(){
        Alarm alarm = new Alarm();
        alarm.setRead(0);
        alarm.setRequestIdx(1);

        Alarm alarm1 = alarmRepository.save(alarm);

        assertThat(alarm).isEqualTo(alarm1);


    }

}