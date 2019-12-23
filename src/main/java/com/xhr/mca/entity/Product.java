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
 * 商品表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 商品名称 **/
	@Column(name = "product_name")
	private String productName;
	/** 商品描述 **/
	@Column(name = "description")
	private String description;
	/** 商品编号 **/
	@Column(name = "product_sn")
	private String productSn;
	/** 商品分类ID **/
	@Column(name = "category_id")
	private Long categoryId;
	/** 0:普通商品;1:零售未来商品;2:会员专区商品 **/
	@Column(name = "type")
	private Integer type;
	/** 商家ID **/
	@Column(name = "seller_id")
	private Long sellerId;
	/** 商品价格 **/
	@Column(name = "price")
	private BigDecimal price;
	/** 币种Id **/
	@Column(name = "coin_id")
	private Long coinId;
	/** 商品价格(币数)不以实时币价更新商品价格 **/
	@Column(name = "coin_price")
	private BigDecimal coinPrice;
	/** 支付方式：0->币种支付(SPU)；1->其他渠道 **/
	@Column(name = "pay_type")
	private Integer payType;
	/** 商品封面图 **/
	@Column(name = "img_url")
	private String imgUrl;
	/** 订单类型：0->正常订单；1->秒杀订单 **/
	@Column(name = "product_type")
	private Integer productType;
	/** 运费 **/
	@Column(name = "freight")
	private Float freight;
	/** 商品重量，默认为克 **/
	@Column(name = "weight")
	private Float weight;
	/** 上架状态：1->上架 0->下架 **/
	@Column(name = "publish_status")
	private Integer publishStatus;
	/** 库存 **/
	@Column(name = "stock")
	private Integer stock;
	/** 单位 **/
	@Column(name = "unit")
	private String unit;
	/** 审核状态：0->未审核；1->审核通过 :2->未通过 **/
	@Column(name = "verify_status")
	private Integer verifyStatus;
	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;
	/** 审核时间 **/
	@Column(name = "verify_time")
	private Long verifyTime;
	/** 商品最后更新时间 **/
	@Column(name = "update_time")
	private Long updateTime;
	/** 销量 **/
	@Column(name = "sale")
	private Float sale;
	/** 新品状态:0->不是新品；1->新品 **/
	@Column(name = "new_status")
	private Integer newStatus;
	/** 0:不限购 ，其他值为限购的具体数量 **/
	@Column(name = "limit_num")
	private Integer limitNum;
	/** 以逗号分割的产品服务：如 : 1->无忧退货；2->快速退款；3->免费包邮 **/
	@Column(name = "service_ids")
	private String serviceIds;
	
	@Transient
	private String coinString;

	public Product() {

	}

	public Product(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	public Product(String productSn) {
		this.productSn = productSn;
	}

}
