package com.dependent.guardian.entity.alarm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmJpaRepository extends JpaRepository<Alarm, Integer>, AlarmRepository {

}
