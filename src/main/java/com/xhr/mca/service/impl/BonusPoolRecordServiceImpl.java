package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.BonusPoolRecord;
import com.xhr.mca.mapper.BonusPoolRecordMapper;
import com.xhr.mca.service.BonusPoolRecordService;

@Service
public class BonusPoolRecordServiceImpl implements BonusPoolRecordService {

	private final BonusPoolRecordMapper bonusPoolRecordMapper;

	@Autowired
	public BonusPoolRecordServiceImpl(BonusPoolRecordMapper bonusPoolRecordMapper) {
		this.bonusPoolRecordMapper = bonusPoolRecordMapper;
	}

	@Override
	public List<BonusPoolRecord> selectAll(BonusPoolRecord bpr) throws WebAppException {
		return bonusPoolRecordMapper.select(bpr);
	}

	@Override
	public void insert(BonusPoolRecord bpr) throws WebAppException {
		bonusPoolRecordMapper.insert(bpr);
	}

}
