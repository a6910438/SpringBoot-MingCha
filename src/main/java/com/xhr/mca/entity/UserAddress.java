package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 用户收款地址表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "user_address")
public class UserAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 用户ID **/
	@Column(name = "user_id")
	private Long userId;

	/** 收款人 **/
	@Column(name = "name")
	private String name;

	/** 是否为默认 **/
	@Column(name = "is_default")
	private Integer isDefault;

	/** 手机号 **/
	@Column(name = "phone")
	private String phone;

	/** 区域 **/
	@Column(name = "area")
	private String area;

	/** 详情地址 **/
	@Column(name = "detail_address")
	private String detailAddress;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	public UserAddress() {
		// TODO Auto-generated constructor stub
	}
	
	public UserAddress(Long userId) {
		this.userId = userId;
	}

	public UserAddress(Long userId, String name, Integer isDefault, String phone, String area, String address) {
		this.userId = userId;
		this.name = name;
		this.isDefault = isDefault;
		this.phone = phone;
		this.area = area;
		this.detailAddress = address;
	}

	public UserAddress(Long userId, Long id, String name, Integer isDefault, String phone, String area,
			String detailAddress) {
		this.id = id;
		this.name = name;
		this.isDefault = isDefault;
		this.phone = phone;
		this.area = area;
		this.detailAddress = detailAddress;
	}

}
