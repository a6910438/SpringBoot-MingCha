package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Deposit;

public interface DepositService {

	public void insert(Deposit deposit) throws WebAppException;
	
	public List<Deposit> selectAll(Deposit deposit) throws WebAppException;

}
