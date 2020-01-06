package com.xhr.mca.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xhr.mca.entity.constant.Status;

import lombok.Data;

/**
 * 币种表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "coin")
public class Coin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 币种全称 **/
	@Column(name = "full_name")
	private String fullName;

	/** 币种简称 **/
	@Column(name = "resume_name")
	private String resumeName;

	/** 币种名称(显示时查看) **/
	@Column(name = "name")
	private String name;

	/** 小数位数 **/
	@Column(name = "number")
	private Integer number;

	/** 汇率 **/
	@Column(name = "rate")
	private BigDecimal rate;

	/** 币种图标URL **/
	@Column(name = "icon_url")
	private String iconUrl;
	
	/** 合约地址 **/
	@Column(name = "contract_address")
	private String contractAddress;

	/** 描述 **/
	@Column(name = "remark")
	private String remark;
	
	/** 手续费 **/
	@Column(name = "fee")
	private Double fee;

	/** 用户创建时是否自动分配地址 **/
	@Column(name = "auto")
	private Integer auto;

	/** 状态 **/
	@Column(name = "status")
	private Integer status;

	/** 排序 **/
	@Column(name = "sort")
	private Integer sort;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	/** 更新时间 **/
	@Column(name = "update_time")
	private Long updateTime;

	@Transient
	private String autoString;
	@Transient
	private String statusString;

	public static String autoString(Status status) {
		switch (status) {
		case YES:
			return "是";
		case NO:
			return "否";
		default:
			return "否";
		}
	}

	public Coin() {
	}

	public Coin(Long id) {
		this.id = id;
	}

	public Coin(String name) {
		this.name = name;
	}

	public Coin(Integer status) {
		this.status = status;
	}
}
