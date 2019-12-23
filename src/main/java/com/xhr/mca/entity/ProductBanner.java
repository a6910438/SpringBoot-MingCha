package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 商品轮播图表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "product_banner")
public class ProductBanner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 商品编号 **/
	@Column(name = "product_sn")
	private String productSn;

	/** 轮播图片URL **/
	@Column(name = "img_url")
	private String imgUrl;

	/** 状态 **/
	@Column(name = "status")
	private Integer status;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	public ProductBanner() {

	}

	public ProductBanner(String productSn, Integer status) {
		this.productSn = productSn;
		this.status = status;
	}
}
