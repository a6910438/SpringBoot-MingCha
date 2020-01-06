package com.xhr.mca.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * 订单表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "mca_order")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 订单编号 **/
	@Column(name = "order_sn")
	private String orderSn;
	/** 商品编号 **/
	@Column(name = "product_sn")
	private String productSn;
	/** 买家用户ID **/
	@Column(name = "user_id")
	private Long userId;
	/** 支付方式：0->未支付；1->币种支付(SPU)；2->微信; 3->支付宝 **/
	@Column(name = "pay_type")
	private Integer payType;
	/** 支付单号 **/
	@Column(name = "payment_id")
	private String paymentId;
	/** 币种ID **/
	@Column(name = "coin_id")
	private Long coinId;
	/** 订单状态（0：新订单；1：待付款；2：待发货；3：待收货；4：已取消；5：已完成；） **/
	@Column(name = "order_status")
	private Integer orderStatus;
	/** 支付状态: 0,未支付;1,已支付 **/
	@Column(name = "pay_status")
	private Integer payStatus;
	/** 0:普通订单;1,零售未来订单 **/
	@Column(name = "order_type")
	private Integer orderType;
	/** 订单数量 **/
	@Column(name = "number")
	private Integer number;
	/** 单价 **/
	@Column(name = "amount")
	private BigDecimal amount;
	/** 本应付金额 **/
	@Column(name = "order_amount")
	private BigDecimal orderAmount;
	/** 实际支付金额 **/
	@Column(name = "pay_amount")
	private BigDecimal payAmount;
	/** 收货地址ID **/
	@Column(name = "address_id")
	private Long addressId;
	/** 用户收货地址 **/
	@Column(name = "user_address")
	private String userAddress;
	/** 用户收货电话 **/
	@Column(name = "address_phone")
	private String addressPhone;
	/** 收货人名称 **/
	@Column(name = "address_user_name")
	private String addressUserName;
	/** 订单取消原因 **/
	@Column(name = "cancel_reason")
	private String cancelReason;
	/** 订单备注 **/
	@Column(name = "note")
	private String note;
	/** 确认收货状态：0->未确认；1->已确认 **/
	@Column(name = "confirm_status")
	private Integer confirmStatus;
	/** 删除状态：0->未删除；1->已删除 **/
	@Column(name = "is_delete")
	private Integer isDelete;
	/** 订单创建时间 **/
	@Column(name = "create_time")
	private Long createTime;
	/** 支付时间 **/
	@Column(name = "payment_time")
	private Long paymentTime;
	/** 发货时间 **/
	@Column(name = "ship_time")
	private Long shipTime;
	/** 确认收货时间 **/
	@Column(name = "signing_time")
	private Long signingTime;
	/** 完成时间 **/
	@Column(name = "complete_time")
	private Long completeTime;
	/** 物流公司 **/
	@Column(name = "logistics_name")
	private String logisticsName;
	/** 物流单号 **/
	@Column(name = "logistics_no")
	private String logisticsNo;
	/** 修改时间 **/
	@Column(name = "update_time")
	private Long updateTime;

	@Transient
	private Long pid;
	@Transient
	private String payPassword;
	@Transient
	private String pic;
	@Transient
	private String productName;
	@Transient
	private String productDesc;
	@Transient
	private Float freight;
	@Transient
	private String coinString;
	@Transient
	private String coinImgUrl;

	public Order() {

	}

	public Order(Long userId, Integer status) {
		this.userId = userId;
		this.orderStatus = status;
	}

	public Order(String orderSn, Integer status, String cancelReason) {
		this.orderSn = orderSn;
		this.orderStatus = status;
		this.cancelReason = cancelReason;
	}

	public Order(Long userId, String productSn, Long addressId, Integer number, String note) {
		this.userId = userId;
		this.productSn = productSn;
		this.addressId = addressId;
		this.number = number;
		this.note = note;
	}

	public Order(String orderSn, Long userId, String payPassword) {
		this.orderSn = orderSn;
		this.userId = userId;
		this.payPassword = payPassword;
	}

	public Order(String orderSn) {
		this.orderSn = orderSn;
	}

}
