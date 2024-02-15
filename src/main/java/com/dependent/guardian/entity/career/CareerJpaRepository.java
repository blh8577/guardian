package com.dependent.guardian.entity.career;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerJpaRepository extends JpaRepository<Career,Integer>,CareerRepository {

}
