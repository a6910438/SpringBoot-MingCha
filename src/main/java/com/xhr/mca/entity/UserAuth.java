package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 用户身份认证表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "user_auth")
public class UserAuth {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 用户ID **/
	@Column(name = "user_id")
	private Long userId;

	/** 身份证名称 **/
	@Column(name = "idcard_name")
	private String idcardName;

	/** 身份证号码 **/
	@Column(name = "idcard")
	private String idcard;

	/** 性别 **/
	@Column(name = "sex")
	private Integer sex;

	/** 护照类型 **/
	@Column(name = "cert_type")
	private Integer certType;

	/** 正面图片URL **/
	@Column(name = "front_img_url")
	private String frontImgUrl;

	/** 背面图片URL **/
	@Column(name = "back_img_url")
	private String backImgUrl;

	/** 人脸图片URL **/
	@Column(name = "face_img_url")
	private String faceImgUrl;

	/** 是否审核通过 **/
	@Column(name = "status")
	private Integer status;

	/** 原因 **/
	@Column(name = "remark")
	private String remark;

	/** 国家 **/
	@Column(name = "country")
	private String country;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	@JSONField(serialize = false)
	@Transient
	private String frontImgFile;
	@JSONField(serialize = false)
	@Transient
	private String backImgFile;
	@JSONField(serialize = false)
	@Transient
	private String faceImgFile;

	public UserAuth() {

	}

	public UserAuth(Long id, Long userId, String idcardName, String idcard, Integer sex, Integer certType,
			String frontImgFile, String backImgFile, String faceImgFile, Integer status,
			String remark, String country, Long createTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.idcardName = idcardName;
		this.idcard = idcard;
		this.sex = sex;
		this.certType = certType;
		this.backImgFile = backImgFile;
		this.faceImgFile = faceImgFile;
		this.frontImgFile = frontImgFile;
		this.status = status;
		this.remark = remark;
		this.country = country;
		this.createTime = createTime;
	}
	
	public UserAuth(Long userId) {
		this.userId = userId;
	}

}
