package com.dependent.guardian.entity.record;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordJpaRepository extends JpaRepository<Record, Integer>, RecordRepository  {
}
