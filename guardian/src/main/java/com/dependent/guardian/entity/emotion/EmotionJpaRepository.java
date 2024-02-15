package com.dependent.guardian.entity.emotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmotionJpaRepository extends JpaRepository<Emotion, Integer>, EmotionRepository {

}
