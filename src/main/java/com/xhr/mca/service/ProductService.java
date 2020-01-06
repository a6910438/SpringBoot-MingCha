package com.xhr.mca.service;

import java.util.List;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Product;
import com.xhr.mca.entity.vo.ProductVo;

public interface ProductService {

	public List<Product> selectAll(Product product) throws WebAppException;
	
	public Product findProductById(Long id) throws WebAppException;
	
	public Product findProductBySn(String sn) throws WebAppException;
	
	public ProductVo selectProductDetail(String sn) throws WebAppException;
	
	public void update(Product p) throws WebAppException;
	
}
