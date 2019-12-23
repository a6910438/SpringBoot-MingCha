package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Coin;

public interface CoinService {

	public void insert(Coin coin) throws WebAppException;
	
	public void update(Coin coin) throws WebAppException;
	
	public List<Coin> selectAll() throws WebAppException;
	
	public Coin getCoinById(Long id) throws WebAppException;
	
	public Coin getCoinByName(String name) throws WebAppException;
	
}
