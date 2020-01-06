package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.OldRecord;

public interface OldRecordService {

	public void insert(OldRecord record) throws WebAppException;

	public List<OldRecord> selectAll(OldRecord record) throws WebAppException;
}
