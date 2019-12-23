package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Coin;
import com.xhr.mca.mapper.CoinMapper;
import com.xhr.mca.service.CoinService;

@Service
public class CoinServiceImpl implements CoinService {

	@Autowired
	private CoinMapper coinMapper;

	@Override
	@Transactional
	public void insert(Coin coin) throws WebAppException {
		coinMapper.insert(coin);
	}

	@Override
	@Transactional
	public void update(Coin coin) throws WebAppException {
		coinMapper.updateByPrimaryKeySelective(coin);
	}

	@Override
	public List<Coin> selectAll() throws WebAppException {
		return coinMapper.selectAll();
	}

	@Override
	public Coin getCoinById(Long id) throws WebAppException {
		return coinMapper.selectByPrimaryKey(id);
	}

	@Override
	public Coin getCoinByName(String name) throws WebAppException {
		return coinMapper.selectOne(new Coin(name));
	}
	
}
