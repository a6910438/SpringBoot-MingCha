package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Deposit;
import com.xhr.mca.mapper.DepositMapper;
import com.xhr.mca.service.DepositService;

@Service
public class DepositServiceImpl implements DepositService {

	private final DepositMapper depositMapper;
	
	@Autowired
	public DepositServiceImpl(DepositMapper depositMapper) {
		this.depositMapper = depositMapper;
	}
	
	@Override
	@Transactional
	public void insert(Deposit deposit) throws WebAppException {
		depositMapper.insert(deposit);
	}

	@Override
	public List<Deposit> selectAll(Deposit deposit) throws WebAppException {
		return depositMapper.select(deposit);
	}

}
