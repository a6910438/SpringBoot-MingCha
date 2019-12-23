package com.xhr.mca.entity.vo;

import java.math.BigDecimal;
import java.util.List;

import com.xhr.mca.entity.UserAssest;

import lombok.Data;

@Data
public class UserAssestVo {

	/** 资产列表 **/
	private List<UserAssest> vos;
	/** 总资产 **/
	private BigDecimal total;

}
