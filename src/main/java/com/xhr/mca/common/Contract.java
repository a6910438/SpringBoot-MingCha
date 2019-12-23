package com.xhr.mca.common;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class Contract {
	// 合约精度
	private String decimals;
	// 合约地址
	private String address;
	private BigInteger gasLimit;
	private String eventTopic0;

	public EthConvert.Unit getUnit() {
		if (StringUtils.isEmpty(decimals))
			return EthConvert.Unit.ETHER;
		else
			return EthConvert.Unit.fromString(decimals);
	}
}
