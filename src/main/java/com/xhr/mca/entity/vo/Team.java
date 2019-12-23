package com.xhr.mca.entity.vo;

/**
 * 团队信息
 * 
 * @author xhr
 *
 */
public class Team {

	private String img;
	private String phone;
	private String nickname;
	private Long userId;

	public Team() {
		// TODO Auto-generated constructor stub
	}

	public Team(String img, String phone, String nickname, Long userId) {
		this.img = img;
		this.phone = phone;
		this.nickname = nickname;
		this.userId = userId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
