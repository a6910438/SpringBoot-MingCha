package com.xhr.mca.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 分红明细
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "bonus_pool_record")
public class BonusPoolRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 用户 **/
	@Column(name = "user_id")
	private Long userId;

	/** 币种 **/
	@Column(name = "coin_id")
	private Long coinId;

	/** 可分红数量 **/
	@Column(name = "amount")
	private BigDecimal amount;
	
	/** 方向(0.存放 1.分红) **/
	@Column(name = "direction")
	private Integer direction;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	public BonusPoolRecord() {

	}

}
