package com.dependent.guardian.service.alarm;

import com.dependent.guardian.entity.alarm.Alarm;
import com.dependent.guardian.entity.alarm.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService{

    private final AlarmRepository alarmRepository;


    @Override
    public Boolean saveAlarm(Alarm alarm) {
        Boolean result = false;
        Alarm save = alarmRepository.save(alarm);

        if (save.getAlarmIdx() != null){
            result = true;

        }
        return result;

    }
}
