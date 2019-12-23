package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Seller;
import com.xhr.mca.mapper.SellerMapper;
import com.xhr.mca.service.SellerService;

@Service
public class SellerServiceImpl implements SellerService {

	private final SellerMapper sellerMapper;

	@Autowired
	public SellerServiceImpl(SellerMapper sellerMapper) {
		this.sellerMapper = sellerMapper;
	}

	@Override
	@Transactional
	public void insert(Seller seller) throws WebAppException {
		sellerMapper.insert(seller);
	}

	@Override
	public List<Seller> selectAll(Seller seller) throws WebAppException {
		return sellerMapper.select(seller);
	}

	@Override
	@Transactional
	public void update(Seller seller) throws WebAppException {
		sellerMapper.updateByPrimaryKeySelective(seller);
	}

}
