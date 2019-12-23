package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.UserAddress;
import com.xhr.mca.mapper.UserAddressMapper;
import com.xhr.mca.service.UserAddressService;

@Service
public class UserAddressServiceImpl implements UserAddressService {

	@Autowired
	private UserAddressMapper userAddressMapper;

	@Override
	public void insert(UserAddress userAddress) throws WebAppException {
		userAddress.setCreateTime(Utility.currentTimestamp());
		userAddressMapper.insert(userAddress);
	}

	@Override
	public void delete(long id) throws WebAppException {
		userAddressMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<UserAddress> findAllByUserId(long userId) throws WebAppException {
		return userAddressMapper.select(new UserAddress(userId));
	}

	@Override
	public UserAddress findAddressById(long id) throws WebAppException {
		return userAddressMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(UserAddress userAddress) throws WebAppException {
		userAddressMapper.updateByPrimaryKeySelective(userAddress);
	}

}
