package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.RmbRecord;

public interface RmbRecordService {

	public void insert(RmbRecord record) throws WebAppException;
	
	public List<RmbRecord> selectAll(RmbRecord record) throws WebAppException;
	
}
