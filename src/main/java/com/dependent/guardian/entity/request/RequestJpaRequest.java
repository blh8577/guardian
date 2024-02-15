package com.dependent.guardian.entity.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RequestJpaRequest extends JpaRepository<Request, Integer>, RequestRepository {

}
