package com.xhr.mca.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionVo {

	/** 明细ID **/
	private Long id;
	/** 业务类型 **/
	private String type;
	/** 金额 **/
	private BigDecimal amount;
	/** 方向(正,负) **/
	private String direction;
	/** 发送ID **/
	private Long send;
	/** 接收ID **/
	private Long receice;
	/** 交易时间 **/
	private Long createTime;
	
	/** 业务类型(int) **/
	private Integer typeI;

}
