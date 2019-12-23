package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.ProductCategory;

public interface ProductCategoryService {

	public void insert(ProductCategory productCategory) throws WebAppException;

	public List<ProductCategory> selectAll(ProductCategory productCategory) throws WebAppException;

	public void update(ProductCategory productCategory) throws WebAppException;

}
