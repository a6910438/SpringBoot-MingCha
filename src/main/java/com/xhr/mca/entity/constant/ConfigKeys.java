package com.xhr.mca.entity.constant;

public enum ConfigKeys {

	CLIENT_VERSION("client_version"), ANDROID_CLIENT_VERSION("android_client_version"),
	IOS_CLIENT_VERSION("ios_client_version"), AGREEMENT_URL("agreement_url"),
	ANDROID_DOWNLOAD_URL("android_download_url"), IOS_DOWNLOAD_URL("ios_download_url"),
	AGREEMENT_ENGLISH_URL("agreement_english_url"), GROUP_URL("group_url"), SHOPPING_URL("shopping_url"),
	RECOMMENDATION_CODE_MUST_BE_FILLED_IN("recommendation_code_must_be_filled_in"), IS_LEVEL_1_BUY("is_level_1_buy"),
	DISCOUNT_1("discount_1"), IS_LEVEL_2_BUY("is_level_2_buy"), DISCOUNT_2("discount_2"),
	TEAM_RECRUIT_QRCODE_URL("team_recruit_qrcode_url"), SUPER_NODE_TEAM_NUMBER("super_node_team_number"),
	SUPER_NODE_INVITE_NUMBER("super_node_invite_number"), GOLD_USER_PAY_AMOUNT("gold_user_pay_amount"),
	PARTNET_USER_PAY_AMOUNT("partnet_user_pay_amount"), GOLD_DIRECT_PUSH_PROFIT("gold_direct_push_profit"),
	PARTNET_DIRECT_PUSH_PROFIT("partnet_direct_push_profit"),
	PARTNET_DIRECT_PUSH_PROFIT_TYPE("partnet_direct_push_profit_type"),
	GOLD_DIRECT_PUSH_PROFIT_TYPE("gold_direct_push_profit_type"), TEAM_PROFIT("team_profit"),
	LEVEL_PROFIT("level_profit"), EVERY_SYSTEM_BONUS_POOL("every_system_bonus_pool"),
	SYSTEM_BONUS_POOL("system_bonus_pool"),
	THAN_WITHDRAW_NUMBER("than_withdraw_number");

	private String key;

	private ConfigKeys(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
