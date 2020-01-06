package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.OldRecord;
import com.xhr.mca.mapper.OldRecordMapper;
import com.xhr.mca.service.OldRecordService;

@Service
public class OldRecordServiceImpl implements OldRecordService {

	private final OldRecordMapper oldRecordMapper;

	@Autowired
	public OldRecordServiceImpl(OldRecordMapper oldRecordMapper) {
		this.oldRecordMapper = oldRecordMapper;
	}

	@Override
	public void insert(OldRecord record) throws WebAppException {
		oldRecordMapper.insert(record);
	}

	@Override
	public List<OldRecord> selectAll(OldRecord record) throws WebAppException {
		return oldRecordMapper.select(record);
	}

}
