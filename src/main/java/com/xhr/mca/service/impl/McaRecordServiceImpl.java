package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.McaRecord;
import com.xhr.mca.mapper.McaRecordMapper;
import com.xhr.mca.service.McaRecordService;

@Service
public class McaRecordServiceImpl implements McaRecordService {

	private final McaRecordMapper mcaRecordMapper;

	@Autowired
	public McaRecordServiceImpl(McaRecordMapper mcaRecordMapper) {
		this.mcaRecordMapper = mcaRecordMapper;
	}

	@Override
	public void insert(McaRecord mcaRecord) throws WebAppException {
		mcaRecordMapper.insert(mcaRecord);
	}

	@Override
	public List<McaRecord> selectAll(McaRecord mcaRecord) throws WebAppException {
		return mcaRecordMapper.select(mcaRecord);
	}

}
