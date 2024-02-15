package com.dependent.guardian.service.alarm;

import com.dependent.guardian.entity.alarm.Alarm;
import com.dependent.guardian.entity.alarm.AlarmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlarmServiceImplTest {

    @Mock
    private AlarmRepository alarmRepository;

    @InjectMocks
    private AlarmServiceImpl alarmService;


    @Test
    void saveAlarm() {
        //given
        Alarm alarm = new Alarm();
        alarm.setRequestIdx(1);
        alarm.setRead(0);
        alarm.setAlarmIdx(1);

        when(alarmRepository.save(any())).thenReturn(alarm);
        //alarmRepository.save 메소드를 쓸때 어떤것이든(any())들어가면 무조건 alarm(위에서 만든) 객체를 반환하라

        //when
        Boolean result = alarmService.saveAlarm(alarm);

        //then
        assertThat(result).isTrue();

    }
}