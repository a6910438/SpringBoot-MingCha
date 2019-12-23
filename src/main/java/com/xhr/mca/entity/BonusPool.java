package com.xhr.mca.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 系统分红池
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "bonus_pool")
public class BonusPool {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 币种 **/
	@Column(name = "coin_id")
	private Long coinId;

	/** 可分红数量 **/
	@Column(name = "may_number")
	private BigDecimal mayNumber;

	/** 已分红数量 **/
	@Column(name = "too_number")
	private BigDecimal tooNumber;

	/** 分红开关(0.关 1.开) **/
	@Column(name = "open")
	private Integer open;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	public BonusPool() {

	}

	public BonusPool(Long coinId) {
		this.coinId = coinId;
	}

}
