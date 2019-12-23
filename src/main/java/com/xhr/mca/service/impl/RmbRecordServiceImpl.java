package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.RmbRecord;
import com.xhr.mca.mapper.RmbRecordMapper;
import com.xhr.mca.service.RmbRecordService;

@Service
public class RmbRecordServiceImpl implements RmbRecordService {

	private final RmbRecordMapper rmbRecordMapper;

	@Autowired
	public RmbRecordServiceImpl(RmbRecordMapper rmbRecordMapper) {
		this.rmbRecordMapper = rmbRecordMapper;
	}

	@Override
	public void insert(RmbRecord record) throws WebAppException {
		rmbRecordMapper.insert(record);
	}

	@Override
	public List<RmbRecord> selectAll(RmbRecord record) throws WebAppException {
		return rmbRecordMapper.select(record);
	}

}
