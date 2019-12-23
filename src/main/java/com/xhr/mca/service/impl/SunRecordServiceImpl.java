package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.SunRecord;
import com.xhr.mca.mapper.SunRecordMapper;
import com.xhr.mca.service.SunRecordService;

@Service
public class SunRecordServiceImpl implements SunRecordService {

	private final SunRecordMapper spuRecordMapper;

	@Autowired
	public SunRecordServiceImpl(SunRecordMapper spuRecordMapper) {
		this.spuRecordMapper = spuRecordMapper;
	}

	@Override
	public void insert(SunRecord record) throws WebAppException {
		spuRecordMapper.insert(record);
	}

	@Override
	public List<SunRecord> selectAll(SunRecord record) throws WebAppException {
		return spuRecordMapper.select(record);
	}

}
