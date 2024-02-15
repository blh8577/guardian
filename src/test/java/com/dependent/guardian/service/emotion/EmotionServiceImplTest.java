package com.dependent.guardian.service.emotion;

import com.dependent.guardian.entity.emotion.Emotion;
import com.dependent.guardian.entity.emotion.EmotionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmotionServiceImplTest {

    @Mock
    EmotionRepository emotionRepository;

    @InjectMocks
    EmotionServiceImpl emotionService;

    @Test
    void isHaveEmotion() {
        List<Emotion> emotionList = new ArrayList<>();
        Emotion emotion = new Emotion();
        //given
        when(emotionRepository.findByReadAndRequestIdx(any(), any())).thenReturn(emotionList);

        //when
        Boolean beforeResult = emotionService.isHaveEmotion(1);
        emotionList.add(emotion);
        Boolean afterResult = emotionService.isHaveEmotion(1);

        //then
        assertThat(beforeResult).isFalse();
        assertThat(afterResult).isTrue();


    }

    @Test
    void updateRead() {
        //given
        List<Emotion> emotionList = new ArrayList<>();

        Emotion emotion1 = new Emotion();
        emotion1.setRead(0);
        emotionList.add(emotion1);
        Emotion emotion2 = new Emotion();
        emotion2.setRead(0);
        emotionList.add(emotion2);

        List<Integer> beforeList = emotionList.stream().map(Emotion::getRead).collect(Collectors.toList());
        when(emotionRepository.findByReadAndRequestIdx(any(), any())).thenReturn(emotionList);

        //when
        emotionService.updateRead(1);
        List<Integer> afterList = emotionList.stream().map(Emotion::getRead).collect(Collectors.toList());

        //then

        beforeList.forEach(e -> {
            assertThat(e).isEqualTo(0);
        });

        afterList.forEach(e -> {
            assertThat(e).isEqualTo(1);
        });


    }
}