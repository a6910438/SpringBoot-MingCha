package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.ProductBanner;

public interface ProductBannerService {

	public void insert(ProductBanner banner) throws WebAppException;
	
	public void update(ProductBanner banner) throws WebAppException;

	public List<ProductBanner> selectAll(ProductBanner banner) throws WebAppException;

}
