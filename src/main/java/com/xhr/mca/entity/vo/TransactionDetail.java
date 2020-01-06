package com.xhr.mca.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionDetail {

	/** 主键ID **/
	private long id;
	/** 发送地址(提币没有) **/
	private String from;
	/** 接收地址 **/
	private String to;
	/** 金额 **/
	private BigDecimal amount;
	/** 哈希 **/
	private String hash;
	/** 创建时间 **/
	private long createTime;
	/** 手续费 **/
	private BigDecimal fee;
	/** 审核状态(充值没有) **/
	private String status;

}
