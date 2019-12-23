package com.xhr.mca.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 充值表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "deposit")
public class Deposit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 用户ID **/
	@Column(name = "user_id")
	private Long userId;

	/** 币种ID **/
	@Column(name = "coin_id")
	private Long coin_id;

	/** 接收地址 **/
	@Column(name = "receice")
	private Long receice;

	/** 手续费 **/
	@Column(name = "fee")
	private BigDecimal fee;

	/** 金额 **/
	@Column(name = "amount")
	private BigDecimal amount;

	/** 发送地址 **/
	@Column(name = "send")
	private String send;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	/** 交易哈希 **/
	@Column(name = "hash")
	private String hash;

}
