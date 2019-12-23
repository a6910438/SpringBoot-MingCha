package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Withdraw;
import com.xhr.mca.mapper.WithdrawMapper;
import com.xhr.mca.service.WithdrawService;

@Service
public class WithdrawServiceImpl implements WithdrawService {

	private final WithdrawMapper withdrawMapper;

	@Autowired
	public WithdrawServiceImpl(WithdrawMapper withdrawMapper) {
		this.withdrawMapper = withdrawMapper;
	}

	@Override
	@Transactional
	public void insert(Withdraw withdraw) throws WebAppException {
		withdrawMapper.insert(withdraw);
	}

	@Override
	public List<Withdraw> selectAll(Withdraw withdraw) throws WebAppException {
		return withdrawMapper.select(withdraw);
	}

	@Override
	@Transactional
	public void update(Withdraw withdraw) throws WebAppException {
		withdrawMapper.updateByPrimaryKeySelective(withdraw);
	}

}
