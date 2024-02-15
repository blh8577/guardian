package com.dependent.guardian.service.grade;

import com.dependent.guardian.entity.grade.Grade;
import com.dependent.guardian.entity.request.Request;

public interface GradeService {
    Grade saveGrade(Request request, Integer point);

    //평점을 입력하고 일주일이 지났는지
    Boolean getRecentGrade(Request request);
}
