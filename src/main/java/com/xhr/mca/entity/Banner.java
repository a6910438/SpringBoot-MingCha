package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 轮播图
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "banner")
public class Banner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 图片url **/
	@Column(name = "url")
	private String url;
	/** 标题 **/
	@Column(name = "title")
	private String title;
	/** 排序 **/
	@Column(name = "sort")
	private Integer sort;
	/** 是否启用 **/
	@Column(name = "status")
	private Integer status;
	/** 类型,0:首页轮播图;1:未来零售轮播图 **/
	@Column(name = "type")
	private Integer type;
	/** 创建时间 **/
	@Column(name = "create_time")
	private Integer createTime;
	
	
	public Banner() {
		super();
	}
	
	public Banner(Integer status) {
		this.status = status;
	}
	
	
}
