package com.dependent.guardian.entity.grade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeJpaRepository extends JpaRepository<Grade, Integer>, GradeRepository {


}
