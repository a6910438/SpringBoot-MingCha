package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 商品详情表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "product_details")
public class ProductDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 商品编号 **/
	@Column(name = "product_sn")
	private String productSn;

	/** 商品图片 **/
	@Column(name = "img_url")
	private String imgUrl;

	/** 排序 **/
	@Column(name = "sort")
	private Integer sort;

	/** 状态 **/
	@Column(name = "status")
	private Integer status;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	public ProductDetails() {

	}
	
	public ProductDetails(String productSn) {
		this.productSn = productSn;
	}
}
