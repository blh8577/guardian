package com.dependent.guardian.entity.alarm;

public interface AlarmRepository {

    //긴급 알람 삽입
    Alarm save(Alarm alarm);

}
