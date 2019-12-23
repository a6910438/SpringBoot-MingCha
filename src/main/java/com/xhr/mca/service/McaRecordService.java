package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.McaRecord;

public interface McaRecordService {

	public void insert(McaRecord mcaRecord) throws WebAppException;
	
	public List<McaRecord> selectAll(McaRecord mcaRecord) throws WebAppException;
	
}
