package com.dependent.guardian.entity.emotion;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest    //jpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//내장 db안쓰기옵션
class EmotionJpaRepositoryTest {

    EmotionRepository emotionRepository;

    @Autowired
    public EmotionJpaRepositoryTest(EmotionRepository emotionRepository) {
        this.emotionRepository = emotionRepository;
    }

    @Test
    void existByReadAndRequestIdx() {
        Boolean result = true;
        //given
        dump();

        //when
        List<Emotion> emotionList = emotionRepository.findByReadAndRequestIdx(0, 1);
        if (emotionList.size() == 0 ) {
            result = false;
        }

        //then
        assertThat(result).isTrue();
    }

    @Test
    void findByReadAndRequestIdx(){
        //given
        List<Emotion> emotions = emotionRepository.findByReadAndRequestIdx(0,1);
        dump();

        //when
        List<Emotion> emotions1 = emotionRepository.findByReadAndRequestIdx(0,1);

        //then
        assertThat(emotions1.size()).isGreaterThan(emotions.size());

    }
//
//    @Test
//    void readUpdate(){
//        //given
//        dump();
//        List<Emotion> emotions = emotionRepository.findByReadAndRequestIdx(0,1);
//
//        //when
//        emotions.forEach(emotion -> {
//            System.out.println(emotion);
//            emotion.setRead(1);
//            emotionRepository.save(emotion);
//        });
//        boolean result = emotionRepository.existsByReadAndRequestIdx(0,1);
//
//        //then
//        assertThat(result).isFalse();
//
//    }


    void dump(){
        Emotion emotion = new Emotion();
        emotion.setRead(0);
        emotion.setRequestIdx(1);
        emotionRepository.save(emotion);
    }



}