package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.SunRecord;

public interface SunRecordService {

	public void insert(SunRecord record) throws WebAppException;

	public List<SunRecord> selectAll(SunRecord record) throws WebAppException;

}
