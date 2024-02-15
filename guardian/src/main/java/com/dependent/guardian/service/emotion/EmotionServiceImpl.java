package com.dependent.guardian.service.emotion;

import com.dependent.guardian.entity.emotion.Emotion;
import com.dependent.guardian.entity.emotion.EmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmotionServiceImpl implements EmotionService{

    private final EmotionRepository emotionRepository;


    @Override
    public Boolean isHaveEmotion(Integer request_idx) {
        Boolean result = true;

        List<Emotion> emotionList = emotionRepository.findByReadAndRequestIdx(0, request_idx);

        if (emotionList.size() == 0){
            result = false;
        }
        return result;
    }

    @Override
    public void updateRead(Integer requestIdx) {
        List<Emotion> emotionList = emotionRepository.findByReadAndRequestIdx(0, requestIdx);

        emotionList.forEach(emotion -> {
            emotion.setRead(1);
            emotionRepository.save(emotion);
        });

    }
}
