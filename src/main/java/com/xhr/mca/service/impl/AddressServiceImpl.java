package com.xhr.mca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.mca.common.Constants;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.EthAddress;
import com.xhr.mca.mapper.AddressMapper;
import com.xhr.mca.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	private final AddressMapper addressMapper;

	@Autowired
	public AddressServiceImpl(AddressMapper addressMapper) {
		this.addressMapper = addressMapper;
	}

	@Override
	public void insert(EthAddress address) throws WebAppException {
		addressMapper.insert(address);
	}

	@Override
	public boolean addressThanCondition() throws WebAppException {
		return addressMapper.addressThanCondition(Constants.CONDITION);
	}

	@Override
	public int selectCountByAddress(EthAddress address) throws WebAppException {
		return addressMapper.selectCount(address);
	}

}
