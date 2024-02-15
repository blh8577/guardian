package com.dependent.guardian.entity.worker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerJpaRepository extends JpaRepository<Worker, Integer>, WorkerRepository {

}
