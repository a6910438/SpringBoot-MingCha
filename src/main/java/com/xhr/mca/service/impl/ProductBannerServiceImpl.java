package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.ProductBanner;
import com.xhr.mca.mapper.ProductBannerMapper;
import com.xhr.mca.service.ProductBannerService;

@Service
public class ProductBannerServiceImpl implements ProductBannerService {

	private final ProductBannerMapper productBannerMapper;

	@Autowired
	public ProductBannerServiceImpl(ProductBannerMapper productBannerMapper) {
		this.productBannerMapper = productBannerMapper;
	}

	@Override
	public void insert(ProductBanner banner) throws WebAppException {
		productBannerMapper.insert(banner);
	}

	@Override
	public void update(ProductBanner banner) throws WebAppException {
		productBannerMapper.updateByPrimaryKeySelective(banner);
	}

	@Override
	public List<ProductBanner> selectAll(ProductBanner banner) throws WebAppException {
		return productBannerMapper.select(banner);
	}

}
