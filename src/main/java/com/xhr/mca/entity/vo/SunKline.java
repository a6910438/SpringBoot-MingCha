package com.xhr.mca.entity.vo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * SPU行情
 * 
 * @author xhr
 *
 */
@Data
public class SunKline {

	/** 最高价 **/
	private BigDecimal high;

	/** 成交量 **/
	private BigDecimal vol;

	/** 最后价格(最新价) **/
	private BigDecimal last;

	/** 最低价 **/
	private BigDecimal low;

	/** 买入数量 **/
	private BigDecimal buy;

	/** 卖出数量 **/
	private BigDecimal sell;

	/** 创建时间 **/
	private Long time;

}
