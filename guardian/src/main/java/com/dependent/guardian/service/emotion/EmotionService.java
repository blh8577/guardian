package com.dependent.guardian.service.emotion;

public interface EmotionService {

    Boolean isHaveEmotion(Integer request_idx);
    //감정 요청이 있는지

    void updateRead(Integer requestIdx);
    //실시간 영상 확인하는 페이지에서 필터로 읽음 표시
}
