package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Withdraw;

public interface WithdrawService {
	
	public void insert(Withdraw withdraw) throws WebAppException;
	
	public List<Withdraw> selectAll(Withdraw withdraw) throws WebAppException;
	
	public void update(Withdraw withdraw) throws WebAppException;
	
}
