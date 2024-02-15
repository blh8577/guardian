package com.dependent.guardian.entity.emotion;

import java.util.List;

public interface EmotionRepository {


    //알림 업데이트용 전체 내용 불러오기 - 읽어야할 알람이 있는지 확인가능
    List<Emotion> findByReadAndRequestIdx(Integer read, Integer requestIdx);

    //감정 삽입 - 감정삽입은 라즈베리파이가 하므로 테스트용
    Emotion save(Emotion emotion);

}