package com.dependent.guardian.service.career;

import com.dependent.guardian.entity.career.Career;

import java.util.List;

public interface CareerService {

    List<Career> getCareerList(Integer workerIdx);

    Boolean deleteCareer(Integer careerIdx);

    Boolean saveCareer(Career career);

}
