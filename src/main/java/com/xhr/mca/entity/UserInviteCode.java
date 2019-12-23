package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 用户推荐码表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "user_invite_code")
public class UserInviteCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 手机号 **/
	@Column(name = "phone")
	private String phone;

	/** 创建时间 **/
	@Column(name = "user_id")
	private Long userId;

	/** 推荐码 **/
	@Column(name = "invite_code")
	private String inviteCode;

	/** 创建时间 **/
	@Column(name = "create_time")
	private Long createTime;

	public UserInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public UserInviteCode() {
		// TODO Auto-generated constructor stub
	}

	public UserInviteCode(String phone, String inviteCode, Long createTime, Long userId) {
		super();
		this.phone = phone;
		this.inviteCode = inviteCode;
		this.createTime = createTime;
		this.userId = userId;
	}

	public UserInviteCode(Long userId) {
		this.userId = userId;
	}

}
