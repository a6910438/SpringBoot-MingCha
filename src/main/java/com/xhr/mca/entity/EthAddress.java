package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 钱包地址表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "eth_address")
public class EthAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 钱包地址 **/
	@Column(name = "address")
	private String address;

	/** 是否在使用 **/
	@Column(name = "is_use")
	private Integer isUse;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	public EthAddress() {
	}

	public EthAddress(String address) {
		this.address = address;
	}
}
