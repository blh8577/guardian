package com.dependent.guardian.service.career;

import com.dependent.guardian.entity.career.Career;
import com.dependent.guardian.entity.career.CareerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CareerServiceImpl implements CareerService {

    private final CareerRepository careerRepository;


    //경력 가져오기
    @Override
    public List<Career> getCareerList(Integer workerIdx) {
        return careerRepository.findByWorkerIdx(workerIdx);
    }

    //경력 삭제
    @Override
    public Boolean deleteCareer(Integer careerIdx) {
        careerRepository.deleteById(careerIdx);
        Optional<Career> career = careerRepository.findById(careerIdx);

        return !career.isPresent();

    }

    //경력 저장
    @Override
    public Boolean saveCareer(Career career) {
        Boolean result = false;
        Career save = careerRepository.save(career);

        if (save.getCareerIdx() != null) {
            result = true;
        }

        return result;
    }
}
