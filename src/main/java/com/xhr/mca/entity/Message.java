package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 短信消息表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "sms_message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 区号 **/
	@Column(name = "area")
	private String area;
	/** 手机号 **/
	@Column(name = "phone")
	private String phone;
	/** 模板ID **/
	@Column(name = "temp_id")
	private Integer tempId;
	/** 内容 **/
	@Column(name = "context")
	private String context;
	/** 是否已发送(0.否 1.是) **/
	@Column(name = "status")
	private Integer status;
	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	public Message() {}
}
