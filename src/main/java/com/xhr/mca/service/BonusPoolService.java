package com.xhr.mca.service;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.BonusPool;

public interface BonusPoolService {

	public void update(BonusPool bp) throws WebAppException;
	
	public BonusPool getBonusPoolByCoinId(Long coinId) throws WebAppException;
	
	public BonusPool init(Long coinId) throws WebAppException;
}
