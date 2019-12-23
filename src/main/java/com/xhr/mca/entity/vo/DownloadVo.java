package com.xhr.mca.entity.vo;

import lombok.Data;

@Data
public class DownloadVo {

	/** 邀请码 **/
	private String inviteCode;
	/** 昵称 **/
	private String nickname;
	/** 头像 **/
	private String headImageUrl;
	/** 二维码URL **/
	private String qrcodeUrl;

}
