package com.xhr.mca.entity.constant;

public enum ExceptionConstants {

	EMPTY_PARAME(0, "参数为空"), ERROR_PARAME(0, "参数异常"), DATABASE_UPDATE_EXPECTION(0, "更新失败,服务器异常"),
	SERVER_EXPECTION(0, "服务器异常"), TOKEN_NOT_IS_EXITS(0, "Token 不存在,请重新登录!"), INSUFFICIENT_FUNDS(0, "余额不足,请充值后再购买"),
	VER_CODE_ERROR(0, "验证码错误!"), USER_IS_EXIST(0, "账号已存在,请直接登录!"), INVITE_PERSON_NOTEXIST(0, "推荐人不存在!"),
	USER_NOT_EXIST(0, "账号不存在!"), USER_LOGIN_FAILD(0, "登录失败,请检查账号和密码是否正确!"), PRODUCT_NOT_FOUND(0, "商品下架,请刷新!"),
	ORDER_NOT_FOUND(0, "订单已失效,请联系客服人员!"),
	COIN_NOT_FOUND(0, "币种不存在!"),
	PAY_PASSWORD_FAILD(0, "密码错误!"),
	CREATE_UNIFIED_ORDER(0, "生成预支付订单异常!"),
	PASS_AUTH_ORDER(0, "请先实名认证之后再提交订单!"),
	STOCK_INSUFFICIENT(0, "库存不足,请联系管理人员赶快加货!");

	private Integer errorCode;
	private String errorMessage;

	ExceptionConstants(Integer errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
