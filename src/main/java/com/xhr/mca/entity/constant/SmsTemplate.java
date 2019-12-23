package com.xhr.mca.entity.constant;

public enum SmsTemplate {

	REGISTER_CAPTCHA_CODE(0, "【MCA零售】您正在注册为MCA零售用户，验证码是：%s,该验证码%d分钟有效"),
	FORGOT_CAPTCHA_CODE(1, "【MCA零售】您正在进行找回密码操作，验证码是：%s,该验证码%d分钟有效，如不是本人操作，请勿向他人透漏"),
	UPDATE_CAPTCHA_CODE(2, "【MCA零售】您正在进行修改密码操作，验证码是：%s,该验证码%d分钟有效，如不是本人操作，请勿向他人透漏"),
	UPDATE_PAY_CAPTCHA_CODE(3, "【MCA零售】您正在进行修改支付密码操作，验证码是：%s,该验证码%d分钟有效，如不是本人操作，请勿向他人透漏"),
	CONGRATULATIONS_REGISTER(4, "【MCA零售】恭喜您，已成功注册为MCA零售用户"), LOGIN_PASSWORD_UPDATE_SUCCESS(5, "【MCA零售】登录密码修改成功"),
	PAY_PASSWORD_UPDATE_SUCCESS(6, "【MCA零售】交易密码修改成功");

	private Integer type;
	private String message;

	private SmsTemplate(Integer type, String message) {
		this.type = type;
		this.message = message;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
