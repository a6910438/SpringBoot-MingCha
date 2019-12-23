package com.xhr.mca.service;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.EthAddress;

public interface AddressService {

	public void insert(EthAddress address) throws WebAppException;

	public boolean addressThanCondition() throws WebAppException;

	public int selectCountByAddress(EthAddress address) throws WebAppException;

}
