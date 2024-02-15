package com.dependent.guardian.service.request;

import com.dependent.guardian.entity.member.Member;
import com.dependent.guardian.entity.request.Request;
import com.dependent.guardian.entity.request.RequestRepository;
import com.dependent.guardian.entity.worker.Worker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Override
    public Optional<Request> getRequest(Integer requestIdx) {
        return requestRepository.findById(requestIdx);
    }

    //상태변경
    @Override
    public String changeCondition(Integer requestIdx, String condition) {
        Optional<Request> request = requestRepository.findById(requestIdx);
        String msg = "오류로 인하여 " + condition + "신청이 실패 했습니다.";

        if (request.isPresent()) {
            if (condition.equals("수락")) {
                request.get().setCondition(condition);
                request.get().setStartDate(LocalDateTime.now());
                requestRepository.save(request.get());
                //대기중인 리스트를 찾을 것이기 때문에 먼저 수락으로 바꿔주고, 시작날짜를 업데이트

                List<Request> waitingList = getWaitingList(request.get().getWorkerIdx());

                waitingList.forEach(e -> {
                    e.setCondition("거절");
                    requestRepository.save(e);
                });
                // 요보사에게 신청한 리스트를 전부 거절로 바꿔준다.

                List<Request> requestList = getRequestList(request.get().getMemberIdx());

                requestList.forEach(e -> {
                    e.setCondition("취소");
                    requestRepository.save(e);
                });
                // 보호자가 신청한 리스트를 전부 취소로 바꿔준다.

            } else if (condition.equals("종료")) {
                request.get().setCondition(condition);
                request.get().setEndDate(LocalDateTime.now());
                requestRepository.save(request.get());

            } else {
                // 거절, 취소는 다르지 않아 하나로 묶음
                request.get().setCondition(condition);
                requestRepository.save(request.get());
            }

            msg = "정상적으로 " + condition + "되었습니다.";

        }
        return msg;

    }

    //요청삽입
    @Override
    public Request saveRequest(Request request) {
        request.setDate(LocalDateTime.now());
        request.setCondition("대기중");
        return requestRepository.save(request);
    }

    //리뷰업데이트
    @Override
    public Request updateReview(Integer requestIdx, String review) {
        Optional<Request> request = requestRepository.findById(requestIdx);

        if (request.isPresent()) {
            review = review.replaceAll("\n", "<br>");
            request.get().setReview(review);
            return requestRepository.save(request.get());
        }

        return null;
    }

    //매칭여부 - 보호자, 요보사
    @Override
    public Request getMatching(Object object) {
        List<Request> matching;
        if (object instanceof Member) {
            Member member = (Member) object;
            matching = requestRepository.findByMemberIdxAndCondition(member.getMemberIdx(), "수락");
        } else {
            Worker worker = (Worker) object;
            matching = requestRepository.findByWorkerIdxAndCondition(worker.getWorkerIdx(), "수락");
        }

        if (matching.isEmpty()) {
            return null;
        } else {
            return matching.get(0);
        }

    }

    //신청목록 -
    @Override
    public List<Request> getRequestList(Integer memberIdx) {
        return requestRepository.findByMemberIdxAndCondition(memberIdx, "대기중");
    }

    //과거신청목록
    @Override
    public List<Request> getPastRequestList(Integer memberIdx) {
        List<String> conditionList = new ArrayList<>();
        conditionList.add("수락");
        conditionList.add("대기중");

        return requestRepository.findByMemberIdxAndConditionNotIn(memberIdx, conditionList);
    }

    //수락인 요양보호사의 인덱스들, Worker에서 사용
    //Worker에서 수락된 요청을 가지지 않은 사람들만 뽑을것임
    @Override
    public List<Integer> getImPossibleWorkerList() {
        List<Request> conditionList = requestRepository.findByCondition("수락");
        if (conditionList.size() == 0) {
            Request request = new Request();
            request.setWorkerIdx(0);
            conditionList.add(request);
        }
        //not in은 null이면 무조건 false이기 때문에 0값을 임의로 삽입해줌

        List<Integer> workerIdxList = conditionList.stream().map(Request::getWorkerIdx).collect(Collectors.toList());
        //수락상태를 가진 요보사인덱스들
        log.info("workerIdxList = {}",workerIdxList);

        return workerIdxList;

        //밑에는 일단 주석해봄
//        List<Request> workerNotInList = requestRepository.findByWorkerIdxNotIn(workerIdxList);
//        //수락상태를 가지지 않은 요보사를 가진 Request들
//
//        List<Integer> collect = workerNotInList.stream().map(Request::getWorkerIdx).collect(Collectors.toList());
//        log.info("workerNotInList = {}",collect.toString());
//
//        return workerNotInList.stream().map(Request::getWorkerIdx).collect(Collectors.toList());
//        //수락상태가 아닌 요보사의 인덱스들
    }

    //요보사의 신청받은 요청들
    @Override
    public List<Request> getWaitingList(Integer workerIdx) {
        return requestRepository.findByWorkerIdxAndCondition(workerIdx, "대기중");

    }


    //요보사의 리뷰,평점들
    @Override
    public List<Request> getWorkerReview(Integer workerIdx) {
        return requestRepository.findByWorkerIdxAndCondition(workerIdx, "종료");
    }


    /*이 밑으론 테스트 안해봄*/
    //요보사의 과거 매칭목록
    @Override
    public List<Request> getPastMatchingList(Integer workerIdx) {
        List<String> conditionList = new ArrayList<>();
        conditionList.add("수락");
        conditionList.add("대기중");

        return requestRepository.findByWorkerIdxAndConditionNotIn(workerIdx, conditionList);
    }


}
