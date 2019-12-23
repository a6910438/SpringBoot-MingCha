package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.UserAddress;

public interface UserAddressService {

	public void insert(UserAddress userAddress) throws WebAppException;
	
	public void delete(long id) throws WebAppException;
	
	public List<UserAddress> findAllByUserId(long userId) throws WebAppException;
	
	public UserAddress findAddressById(long id) throws WebAppException;
	
	public void update(UserAddress userAddress) throws WebAppException;
	
}
