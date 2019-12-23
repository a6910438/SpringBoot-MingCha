package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Seller;

public interface SellerService {

	public void insert(Seller seller) throws WebAppException;
	
	public List<Seller> selectAll(Seller seller) throws WebAppException;
	
	public void update(Seller seller) throws WebAppException;
	
}
