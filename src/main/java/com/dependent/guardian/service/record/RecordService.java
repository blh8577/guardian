package com.dependent.guardian.service.record;

import com.dependent.guardian.entity.record.Record;

import java.util.List;

public interface RecordService {

    List<Record> getRecordList(Integer requestIdx);
}
