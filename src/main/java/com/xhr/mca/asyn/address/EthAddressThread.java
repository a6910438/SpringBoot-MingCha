package com.xhr.mca.asyn.address;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.xhr.mca.common.Utility;
import com.xhr.mca.entity.Coin;
import com.xhr.mca.entity.UserAssest;
import com.xhr.mca.entity.constant.Status;
import com.xhr.mca.mapper.AddressMapper;
import com.xhr.mca.mapper.CoinMapper;
import com.xhr.mca.mapper.UserAssestMapper;

/**
 * 多线程跑地址分配
 * 
 * @author xhr
 *
 */
public class EthAddressThread extends Thread {

	private AddressMapper addressMapper;
	private UserAssestMapper userAssestMapper;
	private CoinMapper coinMapper;
	private Long userID;

	public EthAddressThread(AddressMapper addressMapper, CoinMapper coinMapper, UserAssestMapper userAssestMapper,
			Long userID) {
		this.addressMapper = addressMapper;
		this.coinMapper = coinMapper;
		this.userAssestMapper = userAssestMapper;
		this.userID = userID;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<Coin> coins = coinMapper.select(new Coin(Status.YES.ordinal()));
		List<UserAssest> list = new ArrayList<UserAssest>();
		for (Coin c : coins) {
			UserAssest ua = new UserAssest();
			ua.setUserId(userID);
			ua.setAvaBalance(BigDecimal.ZERO);
			ua.setFreezeBalance(BigDecimal.ZERO);
			ua.setCreateTime(Utility.currentTimestamp());
			ua.setUpdateTime(Utility.currentTimestamp());
			ua.setCoinId(c.getId());
			ua.setAddress(addressMapper.getNowAddress());
			addressMapper.updateStatus(ua.getAddress());
			list.add(ua);
		}
		if (Utility.isNotNull(list) && list.size() > 0) {
			userAssestMapper.insertList(list);
		}
	}

}
