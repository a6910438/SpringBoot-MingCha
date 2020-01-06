package com.xhr.mca.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xhr.mca.common.Utility;

import lombok.Data;

/**
 * 用户资产表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "user_assest")
public class UserAssest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 用户ID **/
	@Column(name = "user_id")
	private Long userId;

	/** 币种ID **/
	@Column(name = "coin_id")
	private Long coinId;

	/** 可用余额 **/
	@Column(name = "ava_balance")
	private BigDecimal avaBalance;

	/** 冻结余额 **/
	@Column(name = "freeze_balance")
	private BigDecimal freezeBalance;

	/** 钱包地址 **/
	@Column(name = "address")
	private String address;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	/** 更新时间 **/
	@Column(name = "update_time")
	private Long updateTime;

	/** 币种名称(显示时查看) **/
	@Transient
	private String name;
	/** 小数位数 **/
	@Transient
	private Integer number;
	@Transient
	private BigDecimal rmbMoney;
	@Transient
	private BigDecimal rate;
	@Transient
	private String iconUrl;
	@Transient
	private Double fee;

	public UserAssest() {

	}

	public UserAssest(Long userId) {
		this.userId = userId;
	}

	public UserAssest(Long userID, Long coinID) {
		this.userId = userID;
		this.coinId = coinID;
	}

	public UserAssest(Long id, Long userID, Long coinID, BigDecimal number) {
		this.id = id;
		this.userId = userID;
		this.coinId = coinID;
		this.avaBalance = number;
		this.createTime = Utility.currentTimestamp();
	}

	public UserAssest(Long userID, Long coinID, BigDecimal number) {
		this.userId = userID;
		this.coinId = coinID;
		this.avaBalance = number;
		this.createTime = Utility.currentTimestamp();
	}

}
