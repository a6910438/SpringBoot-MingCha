package com.xhr.mca.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.common.Constants;
import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Coin;
import com.xhr.mca.entity.UserAssest;
import com.xhr.mca.entity.constant.ExceptionConstants;
import com.xhr.mca.entity.constant.Type;
import com.xhr.mca.entity.vo.TransactionDetail;
import com.xhr.mca.entity.vo.TransactionVo;
import com.xhr.mca.entity.vo.UserAssestVo;
import com.xhr.mca.mapper.CoinMapper;
import com.xhr.mca.mapper.UserAssestMapper;
import com.xhr.mca.service.UserAssestService;

@Service
public class UserAssestServiceImpl implements UserAssestService {

	private final UserAssestMapper userAssestMapper;
	private final CoinMapper coinMapper;

	@Autowired
	public UserAssestServiceImpl(UserAssestMapper userAssestMapper, CoinMapper coinMapper) {
		this.userAssestMapper = userAssestMapper;
		this.coinMapper = coinMapper;
	}

	@Override
	public UserAssestVo findAssestsByUserId(Long userID) throws WebAppException {
		UserAssestVo vo = new UserAssestVo();
		List<UserAssest> list = userAssestMapper.findAssestsByUserId(userID);
		BigDecimal total = BigDecimal.ZERO;
		for (UserAssest ua : list) {
			ua.setRate(Utility.isZero(ua.getRate()));
			ua.setAvaBalance(Utility.isZero(ua.getAvaBalance()));
			ua.setFreezeBalance(Utility.isZero(ua.getFreezeBalance()));
			ua.setRmbMoney(Utility.isZero(ua.getAvaBalance().multiply(ua.getRate())));
			total = total.add(ua.getAvaBalance());
			Coin coin = coinMapper.selectByPrimaryKey(ua.getCoinId());
			if (Utility.isNotNull(coin)) {
				ua.setIconUrl(coin.getIconUrl());
				ua.setFee(coin.getFee());
			}
		}
		vo.setVos(list);
		vo.setTotal(total);
		return vo;
	}

	@Override
	@Transactional
	public void addAssest(UserAssest userAssest) throws WebAppException {
		userAssestMapper.insert(userAssest);
	}

	@Override
	public UserAssest findAssestByUserIdAndCoinId(Long userID, Long coinID) throws WebAppException {
		return userAssestMapper.selectOne(new UserAssest(userID, coinID));
	}

	@Override
	@Transactional
	public void updateAssest(UserAssest userAssest) throws WebAppException {
		userAssestMapper.updateByPrimaryKeySelective(userAssest);
	}

	@Override
	public List<TransactionVo> findTransactionsByUserIdAndCoinId(Long userID, Long coinID, Integer type, Integer page,
			Integer rows) throws WebAppException {
		Coin coin = coinMapper.selectByPrimaryKey(coinID);
		if (Utility.isNull(coin)) {
			throw new WebAppException(ExceptionConstants.COIN_NOT_FOUND);
		}

		String direction = "%%";
		List<TransactionVo> vos = new ArrayList<TransactionVo>();
		if (type == Constants.ONE) {
			direction = "+";
		} else if (type == Constants.TWO) {
			direction = "-";
		}

		if (Constants.MCA.equals(coin.getName())) {
			vos = userAssestMapper.findMcaTransactionVos(userID, coinID, direction, (page - 1) * rows, rows);
		} else if (Constants.SUN.equals(coin.getName())) {
			vos = userAssestMapper.findSunTransactionVos(userID, coinID, direction, (page - 1) * rows, rows);
		} else if (Constants.RMB.equals(coin.getName())) {
			vos = userAssestMapper.findRmbTransactionVos(userID, coinID, direction, (page - 1) * rows, rows);
		} else if (Constants.OLD.equals(coin.getName())) {
			vos = userAssestMapper.findOldTransactionVos(userID, coinID, direction, (page - 1) * rows, rows);
		}
		List<TransactionVo> result = new ArrayList<TransactionVo>();
		for (TransactionVo vo : vos) {
			vo.setType(Type.values()[vo.getTypeI()].getName());
			vo.setAmount(Utility.isZero(vo.getAmount()));
			if (StringUtils.isBlank(vo.getDirection())) {
				if (userID == vo.getReceice()) {
					vo.setDirection("+");
				} else {
					vo.setDirection("-");
				}

				if ((("+".equals(vo.getDirection()) && !vo.getDirection().equals(direction))
						|| ("-".equals(vo.getDirection()) && !vo.getDirection().equals(direction))) && !"%%".equals(direction)) {
					continue;
				}
			}
			result.add(vo);
		}

		return result;
	}

	@Override
	public TransactionDetail findTransactionDetailById(Long id, Integer type) throws WebAppException {
		TransactionDetail detail = null;
		if (Type.WITHDRAW.getId() == type) {
			detail = userAssestMapper.findWithdrawDetail(id);
		} else if (Type.DEPOSIT.getId() == type) {
			detail = userAssestMapper.findDepositDetail(id);
		} else {
			throw new WebAppException(ExceptionConstants.TRANSACTION_DETAIL_NOT_FOUND);
		}
		return detail;
	}
}
