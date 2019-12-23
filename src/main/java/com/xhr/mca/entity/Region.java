package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 地区
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "region")
public class Region {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 名称 **/
	@Column(name = "name")
	private String name;

	/** 父ID **/
	@Column(name = "parent_id")
	private Long parentId;

	/** 级别 **/
	@Column(name = "level")
	private Integer level;

	/** 是否为热门城市 1:是 **/
	@Column(name = "is_hot")
	private Integer isHot;

	public Region() {
	}

	public Region(Long parentId) {
		this.parentId = parentId;
	}

}
