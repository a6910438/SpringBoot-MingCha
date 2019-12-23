package com.xhr.mca.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionVo {

	/** 业务类型 **/
	private String type;
	/** 金额 **/
	private BigDecimal amount;
	/** 方向(正,负) **/
	private String direction;
	/** 交易时间 **/
	private Long createTime;
	
	/** 业务类型(int) **/
	private Integer typeI;

}
