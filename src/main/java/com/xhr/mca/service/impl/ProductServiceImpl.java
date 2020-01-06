package com.xhr.mca.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Product;
import com.xhr.mca.entity.ProductBanner;
import com.xhr.mca.entity.ProductDetails;
import com.xhr.mca.entity.constant.ExceptionConstants;
import com.xhr.mca.entity.constant.Status;
import com.xhr.mca.entity.vo.ProductVo;
import com.xhr.mca.mapper.CoinMapper;
import com.xhr.mca.mapper.ProductDetailsMapper;
import com.xhr.mca.mapper.ProductMapper;
import com.xhr.mca.service.ProductBannerService;
import com.xhr.mca.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductMapper productMapper;
	private final ProductBannerService productBannerService;
	private final ProductDetailsMapper productDetailsMapper;
	private final CoinMapper coinMapper;

	@Autowired
	public ProductServiceImpl(ProductMapper productMapper, ProductBannerService productBannerService,
			ProductDetailsMapper productDetailsMapper, CoinMapper coinMapper) {
		this.productMapper = productMapper;
		this.coinMapper = coinMapper;
		this.productBannerService = productBannerService;
		this.productDetailsMapper = productDetailsMapper;
	}

	@Override
	public List<Product> selectAll(Product product) throws WebAppException {
		product.setIsDelete(Status.NO.ordinal());
		List<Product> ps = productMapper.select(product);
		for (Product p : ps) {
			p.setCoinString(coinMapper.selectByPrimaryKey(p.getCoinId()).getName());
			p.setCoinPrice(p.getCoinPrice().setScale(2));

		}
		return ps;
	}

	@Override
	public Product findProductById(Long id) throws WebAppException {
		return productMapper.selectByPrimaryKey(id);
	}

	@Override
	public ProductVo selectProductDetail(String sn) throws WebAppException {
		Product p = findProductBySn(sn);
		if (Utility.isNull(p)) {
			throw new WebAppException(ExceptionConstants.PRODUCT_NOT_FOUND);
		}
		p.setCoinString(coinMapper.selectByPrimaryKey(p.getCoinId()).getName());
		p.setCoinPrice(p.getCoinPrice().setScale(2));
		ProductVo vo = new ProductVo();
		vo.setProduct(p);
		vo.setBanners(productBannerService.selectAll(new ProductBanner(sn, Status.YES.ordinal())));
		vo.setDetails(productDetailsMapper.select(new ProductDetails(sn)));
		return vo;
	}

	@Override
	public Product findProductBySn(String sn) throws WebAppException {
		return productMapper.selectOne(new Product(sn));
	}

	@Override
	public void update(Product p) throws WebAppException {
		productMapper.updateByPrimaryKeySelective(p);
	}

}
