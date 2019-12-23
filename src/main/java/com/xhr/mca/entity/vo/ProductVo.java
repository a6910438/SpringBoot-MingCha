package com.xhr.mca.entity.vo;

import java.util.List;

import com.xhr.mca.entity.Product;
import com.xhr.mca.entity.ProductBanner;
import com.xhr.mca.entity.ProductDetails;

import lombok.Data;

/**
 * 商品明细数据模型
 * 
 * @author xhr
 *
 */
@Data
public class ProductVo {

	/** 商品信息 **/
	private Product product;
	/** 轮播图集合 **/
	private List<ProductBanner> banners;
	/** 商品详情图片 **/
	private List<ProductDetails> details;
}
