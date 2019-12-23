package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 系统配置表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "config")
public class Config {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 唯一键 **/
	@Column(name = "c_key")
	private String key;
	/** 值 **/
	@Column(name = "c_value")
	private String value;
	/** 描述 **/
	@Column(name = "c_desc")
	private String desc;
	/** 状态(是否启用) **/
	@Column(name = "c_status")
	private Integer status;
	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;
	
	public Config() {
		// TODO Auto-generated constructor stub
	}

	public Config(String key) {
		this.key = key;
	}

}
