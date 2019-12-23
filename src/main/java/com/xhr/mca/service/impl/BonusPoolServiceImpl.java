package com.xhr.mca.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.BonusPool;
import com.xhr.mca.entity.constant.Controller;
import com.xhr.mca.mapper.BonusPoolMapper;
import com.xhr.mca.service.BonusPoolService;

@Service
public class BonusPoolServiceImpl implements BonusPoolService {

	private final BonusPoolMapper bonusPoolMapper;

	@Autowired
	public BonusPoolServiceImpl(BonusPoolMapper bonusPoolMapper) {
		this.bonusPoolMapper = bonusPoolMapper;
	}

	@Override
	public void update(BonusPool bp) throws WebAppException {
		bonusPoolMapper.updateByPrimaryKeySelective(bp);
	}

	@Override
	public BonusPool getBonusPoolByCoinId(Long coinId) throws WebAppException {
		return bonusPoolMapper.selectOne(new BonusPool(coinId));
	}

	@Override
	public BonusPool init(Long coinId) throws WebAppException {
		BonusPool bp = new BonusPool();
		bp.setCoinId(coinId);
		bp.setCreateTime(Utility.currentTimestamp());
		bp.setMayNumber(BigDecimal.ZERO);
		bp.setTooNumber(BigDecimal.ZERO);
		bp.setOpen(Controller.Close.ordinal());
		bonusPoolMapper.insert(bp);
		return bp;
	}

}
