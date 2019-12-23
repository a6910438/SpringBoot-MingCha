package com.xhr.mca.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 提币表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "withdraw")
public class Withdraw {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 用户ID **/
	@Column(name = "user_id")
	private Long userId;

	/** 钱包地址 **/
	@Column(name = "wallet_address")
	private String walletAddress;

	/** 交易哈希 **/
	@Column(name = "hash")
	private String hash;

	/** 金额 **/
	@Column(name = "amount")
	private BigDecimal amount;

	/** 手续费 **/
	@Column(name = "commission")
	private BigDecimal commission;

	/** 0待处理，1成功，2驳回 **/
	@Column(name = "status")
	private Integer status;

	/** 提交时间 **/
	@Column(name = "create_time")
	private Long createTime;

	/** 完成时间 **/
	@Column(name = "done_time")
	private Long doneTime;
}
