package com.xhr.mca.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * SUN流水表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "sun_record")
public class SunRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 发送人 **/
	@Column(name = "send")
	private Long send;

	/** 业务类型 **/
	@Column(name = "type")
	private Integer type;

	/** 交易前 **/
	@Column(name = "old")
	private BigDecimal old;

	/** 交易后 **/
	@Column(name = "now")
	private BigDecimal now;

	/** 交易金额 **/
	@Column(name = "amount")
	private BigDecimal amount;

	/** 接收人 **/
	@Column(name = "receice")
	private Long receice;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	/** 交易方向(加或者减) **/
	@Column(name = "direction")
	private String direction;

}
