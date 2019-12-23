package com.xhr.mca.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 商户表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "seller")
public class Seller {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 商家名称 **/
	@Column(name = "name")
	private String name;

	/** 商家账号 **/
	@Column(name = "phone")
	private String phone;

	/** 登录密码 **/
	@Column(name = "password1")
	private String password1;

	/** 支付密码 **/
	@Column(name = "password2")
	private String password2;

	/** 客服电话 **/
	@Column(name = "service_phone")
	private String servicePhone;

	/** 商家项目名称 **/
	@Column(name = "project_name")
	private String projectName;

	/** 商家积分 **/
	@Column(name = "seller_point")
	private BigDecimal sellerPoint;

	/** 商家手机 **/
	@Column(name = "seller_phone")
	private String sellerPhone;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	/** 修改时间 **/
	@Column(name = "update_time")
	private Long updateTime;

	/** 账号状态（0：未激活；1：正常；2：禁用；） **/
	@Column(name = "seller_disable")
	private Integer sellerDisable;

	/** 商家发货地址,省份 **/
	@Column(name = "delivery_province")
	private String deliveryProvince;

	/** 商家发货地址,城市 **/
	@Column(name = "city_province")
	private String cityProvince;

	/** 公司地址 **/
	@Column(name = "compant_address")
	private String compantAddress;

	/** 公司电话 **/
	@Column(name = "compant_phone")
	private String compantPhone;

	/** 公司email **/
	@Column(name = "compant_email")
	private String compantEmail;

	/** 联系人姓名 **/
	@Column(name = "link_name")
	private String linkName;

	/** 联系人电话 **/
	@Column(name = "link_phone")
	private String linkPhone;

	/** 法人姓名 **/
	@Column(name = "legal_name")
	private String legalName;

	/** 法人身份证 **/
	@Column(name = "legal_id")
	private String legalId;

	/** 法人身份证照片 **/
	@Column(name = "legal_img")
	private String legalImg;

	/** 营业执照号 **/
	@Column(name = "license_num")
	private String licenseNum;

	/** 营业执照电子版 **/
	@Column(name = "licence_img")
	private String licenceImg;

	/** 认证状态（0：待认证；1：已认证；2：未通过；） **/
	@Column(name = "company_status")
	private String companyStatus;

	/** 积分模式(0：关，1：开) **/
	@Column(name = "integral_mode")
	private String integralMode;

	/** 是否给用户赠送积分(0：关，1：开) **/
	@Column(name = "whether_or_not_to_give_credit_to_users")
	private String whetherOrNotToGiveCreditToUsers;

	/** 赠送给用户的积分比例（%） **/
	@Column(name = "ratio_of_points_given_to_users")
	private String ratioOfPointsGivenToUsers;
}
