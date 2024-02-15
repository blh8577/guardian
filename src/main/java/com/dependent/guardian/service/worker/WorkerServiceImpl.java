package com.dependent.guardian.service.worker;

import com.dependent.guardian.entity.address.Address;
import com.dependent.guardian.entity.worker.Worker;
import com.dependent.guardian.entity.worker.WorkerRepository;
import com.dependent.guardian.service.function.ImageUploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;
    private final ImageUploaderService imageUploader;

    /* 쿠키에 저장해서 자동로그인 시키기, 로그아웃시 쿠키삭제하기 Controller에서 할지 Service에서 할지 고민할것 */
    @Override
    public Optional<Worker> signIn(String id) {
        return workerRepository.findByUserId(id);
    }


    //이미지는 licenseImg, profileImg로 받아야함 - request를 통해 ImageUploader에서 이름 처리를 해주기때문에 여기선 따로 처리안함
    @Override
    public Worker signUp(Worker worker, HttpServletRequest request) throws ServletException, IOException {
        String license = imageUploader.saveImage(request, "license");
        String selfie = imageUploader.saveImage(request, "selfie");
        if (license == null || selfie == null) {
            return null;
        } else {
            worker.setLicense(license);
            worker.setSelfie(selfie);
            return workerRepository.save(worker);
        }
    }

    //신청 가능한 요보사들
    @Override
    public List<Worker> getWorkerList(List<Integer> workerIdx, Address address) {
        return workerRepository.findByWorkerIdxNotInAndAddress1AndAddress2(workerIdx, address.getAddress1(), address.getAddress2());
    }

    //요보사 한명의 정보
    @Override
    public Optional<Worker> getWorker(Integer workerIdx) {
        return workerRepository.findById(workerIdx);
    }

    //요보사 개인정보 업데이트
    @Override
    public Worker update(Worker worker) {
        Optional<Worker> workerOptional = workerRepository.findById(worker.getWorkerIdx());
        if (workerOptional.isPresent()) {
            workerOptional.get().setPhone(worker.getPhone());
            workerOptional.get().setAddress1(worker.getAddress1());
            workerOptional.get().setAddress2(worker.getAddress2());
            String introduce = worker.getIntroduce().replaceAll("\n", "<br>");
            workerOptional.get().setIntroduce(introduce);

            return workerRepository.save(workerOptional.get());
        }

        return null;
    }

}
