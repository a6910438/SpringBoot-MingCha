package com.xhr.mca.entity.constant;

public enum Type {

	PAY_ORDER(0, "购买订单扣除", "-"), DEPOSIT(1, "充值", "+"), WITHDRAW(2, "提币", "-"), BACK_PROFIT(3, "直推收益", "+"),
	TEAM_PROFIT(4, "团队收益", "+"), LEVEL_PROFIT(5, "平级收益", "+"), BONUS_POOL_PROFIT(6, "分红池奖励", "+"), PAY_ORDER_SEND(7, "购买订单赠送", "+"), TRANSFER(8, "转账", "-");

	private Integer id;
	private String name;
	private String direction;

	private Type(Integer id, String name, String dicString) {
		this.id = id;
		this.name = name;
		this.direction = dicString;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}
