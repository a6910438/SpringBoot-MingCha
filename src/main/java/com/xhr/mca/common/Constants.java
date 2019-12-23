package com.xhr.mca.common;

/**
 * 常量
 * 
 * @author ScienJus
 * @date 2015/7/31.
 */
public class Constants {

	/**
	 * 存储当前登录用户id的字段名
	 */
	public static final String CURRENT_MEMBER_ID = "CURRENT_MEMBER_ID";
	public static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	/**
	 * token有效期（小时）
	 */
	public static final int TOKEN_EXPIRES_HOUR = 30 * 60 * 60;// 天时分秒

	/**
	 * 存放Authorization的header字段
	 */
	public static final String TOKEN = "token";

	// 放入Redis后缀
	public static final String REGIST_SUFFIX = "regist_1";
	public static final String FORGOT_SUFFIX = "forgot_1";
	public static final String UPTPASS_SUFFIX = "uptpass_1";
	public static final String PAYPASS_SUFFIX = "paypass_1";

	// 存放头像路径
	public static final String USER_IMAGE_PATH = "user/images/";
	public static final String PNG_SUFFIX = ".png";
	public static final String JPG_SUFFIX = ".jpg";

	public static final String IDCARD_IMG_PATH = "idcard/idcard";
	public static final String BANNER_IMG_PATH = "banner/idcard";

	// 从一到十
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIFTEEN = 5;
	public static final int SIX = 6;
	public static final int SEVEN = 7;
	public static final int EIGHT = 8;
	public static final int NINE = 9;
	public static final int MINUTE = 60;
	public static final int HOUR = 3600;
	public static final int DAY = 86400;

	public static final String OLD = "OLD";
	public static final String SUN = "SUN";
	public static final String MCA = "MCA";
	public static final String RMB = "RMB";

	// 用户钱包私钥统一密码
	public static final String ETH_PASSWORD = "HuangShengLoveme";

	// 地址是否继续生成根据该条件判断
	public static final Integer CONDITION = 500;

	public static final String ORDER_SN_SUFFIX = "OD";

	public static final int PAGE = 1;
	public static final int ROWS = 10;

}
