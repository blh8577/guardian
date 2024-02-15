package com.dependent.guardian.service.grade;

import com.dependent.guardian.entity.grade.Grade;
import com.dependent.guardian.entity.grade.GradeRepository;
import com.dependent.guardian.entity.request.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;


    @Override
    public Grade saveGrade(Request request, Integer point) {
        Grade grade = new Grade();
        grade.setDate(LocalDateTime.now());
        grade.setWorkerIdx(request.getWorkerIdx());
        grade.setRequestIdx(request.getRequestIdx());
        grade.setPoint(point);

        return gradeRepository.save(grade);
    }

    //평점을 매겨야하는지 - 평점을 안매긴시간이 일주일보다 크거나 같으면 평점을 입력해야함
    @Override
    public Boolean getRecentGrade(Request request) {
        List<Grade> gradeList = gradeRepository.findByRequestIdxOrderByDateDesc(request.getRequestIdx());
        Date nowDate = java.sql.Timestamp.valueOf(LocalDateTime.now());
        Date lastDate;
        long week = 86400000 * 7;
        long result;

        if (!gradeList.isEmpty()) {
            Grade grade = gradeList.get(0);
            lastDate = java.sql.Timestamp.valueOf(grade.getDate());
        }else {
            //평점 목록이 비어있다면 아직 한번도 평점을 매기지 않은것이므로 요청을 시작한 날짜와 비교
            lastDate = java.sql.Timestamp.valueOf(request.getStartDate());
        }
        result = lastDate.getTime() - nowDate.getTime();
        return result>=week;
    }
}
