package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.ProductCategory;
import com.xhr.mca.mapper.ProductCategoryMapper;
import com.xhr.mca.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	private final ProductCategoryMapper productCategoryMapper;

	@Autowired
	public ProductCategoryServiceImpl(ProductCategoryMapper productCategoryMapper) {
		this.productCategoryMapper = productCategoryMapper;
	}

	@Override
	@Transactional
	public void insert(ProductCategory productCategory) throws WebAppException {
		productCategoryMapper.insert(productCategory);
	}

	@Override
	public List<ProductCategory> selectAll(ProductCategory productCategory) throws WebAppException {
		return productCategoryMapper.select(productCategory);
	}

	@Override
	@Transactional
	public void update(ProductCategory productCategory) throws WebAppException {
		productCategoryMapper.updateByPrimaryKeySelective(productCategory);
	}

}
