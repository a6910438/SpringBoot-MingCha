package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.BonusPoolRecord;

public interface BonusPoolRecordService {

	public List<BonusPoolRecord> selectAll(BonusPoolRecord bpr) throws WebAppException;
	
	public void insert(BonusPoolRecord bpr) throws WebAppException;
	
}
