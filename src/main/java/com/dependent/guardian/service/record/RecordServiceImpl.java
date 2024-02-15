package com.dependent.guardian.service.record;

import com.dependent.guardian.entity.record.Record;
import com.dependent.guardian.entity.record.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    @Override
    public List<Record> getRecordList(Integer requestIdx) {
        return recordRepository.findByRequestIdxOrderByDateDesc(requestIdx);
    }
}
