package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 产品分类表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "product_category")
public class ProductCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 分类名称 **/
	@Column(name = "name")
	private String name;
	/** 父ID **/
	@Column(name = "parent_id")
	private Long parentId;
	/** 分类级别：1->1级；2->2级；3->3级 **/
	@Column(name = "level")
	private Integer level;
	/** 是否显示在导航栏：0->不显示；1->显示 **/
	@Column(name = "nav_status")
	private Integer navStatus;
	/** 显示状态：0->不显示；1->显示 **/
	@Column(name = "show_status")
	private Integer showStatus;
	/** 节点集 **/
	@Column(name = "parent_item")
	private String parentItem;
	/** 分类排序 **/
	@Column(name = "category_order")
	private Integer categoryOrder;
	/** 分类图片 **/
	@Column(name = "img_url")
	private String imgUrl;
	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;
}
