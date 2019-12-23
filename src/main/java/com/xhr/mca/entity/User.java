package com.xhr.mca.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.annotation.JSONField;
import com.xhr.mca.entity.constant.Level;
import com.xhr.mca.entity.constant.Node;
import com.xhr.mca.entity.constant.Sex;
import com.xhr.mca.entity.constant.Status;

import lombok.Data;

/**
 * 用户表
 * 
 * @author xhr
 *
 */
@Data
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 手机号 **/
	@Column(name = "phone")
	private String phone;

	/** 邮箱地址 **/
	@Column(name = "email")
	private String email;

	/** 登录密码 **/
	@Column(name = "password1")
	private String password1;

	/** 交易密码 **/
	@Column(name = "password2")
	private String password2;

	/** 会员等级 **/
	@Column(name = "level")
	private Integer level;

	/** 节点等级 **/
	@Column(name = "node_level")
	private Integer nodeLevel;

	/** 性别 **/
	@Column(name = "sex")
	private Integer sex;

	/** 上级ID **/
	@Column(name = "pid")
	private Long pid;

	/** 是否是身份证 **/
	@Column(name = "is_idcard")
	private Integer isIdcard;

	/** 注册时间 **/
	@Column(name = "create_time")
	private Long createTime;

	/** 会员头像URL **/
	@Column(name = "img_url")
	private String imgUrl;

	/** 注册IP **/
	@Column(name = "last_ip")
	private String lastIp;

	/** 启用/停用 **/
	@Column(name = "status")
	private Integer status;

	/** 昵称 **/
	@Column(name = "nickname")
	private String nickname;

	/** 区域代码 **/
	@Column(name = "areacode")
	private String areacode;

	/** 身份证名称 **/
	@Column(name = "idcard_name")
	private String idcardName;

	/** 身份证号码 **/
	@Column(name = "idcard")
	private String idcard;

	/** 支付宝账号 **/
	@Column(name = "alipay_user")
	private String alipayUser;

	/** 最后登录时间 **/
	@Column(name = "update_time")
	private Long updateTime;

	/** 邀请人数统计 **/
	@Column(name = "count_code")
	private Integer countCode;

	@Transient
	private String token;
	@JSONField(serialize = false)
	@Transient
	private String verCode;
	@Transient
	private String inviteCode;
	@JSONField(serialize = false)
	@Transient
	private String repeatpass;
	@JSONField(serialize = false)
	@Transient
	private MultipartFile file;
	/** 0 全部 1 完成 -1 未实名 **/
	@JSONField(serialize = false)
	@Transient
	private Integer cardAuditStatus;
	@JSONField(serialize = false)
	@Transient
	private Integer page;
	@JSONField(serialize = false)
	@Transient
	private Integer rows;
	@Transient
	private String sexString;
	@Transient
	private String levelString;
	@Transient
	private String nodeLevelString;
	@Transient
	private String statusString;
	@Transient
	private String base64FileString;
	@Transient
	private Integer is_pass;

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String phone) {
		this.phone = phone;
	}

	public User(String area, String phone, String password) {
		this.areacode = area;
		this.phone = phone;
		this.password1 = password;
		this.status = Status.YES.ordinal();
	}

	public User(String phone, String password1, String password2, String areaCode, String verCode, String inviteCode,
			String lastIp) {
		this.phone = phone;
		this.areacode = areaCode;
		this.password1 = password1;
		this.password2 = password2;
		this.verCode = verCode;
		this.inviteCode = inviteCode;
		this.lastIp = lastIp;
	}

	public User(Long pid) {
		this.pid = pid;
	}

	public static String nodeLevelString(Node node) {
		switch (node) {
		case General:
			return "节点";
		case High:
			return "高级节点";
		default:
			return "节点";
		}
	}

	public static String levelString(Level level) {
		switch (level) {
		case General:
			return "";
		case GoldUser:
			return "黄金创客";
		case Partnet:
			return "合伙人";
		default:
			return "";
		}
	}

	public static String sexString(Sex sex) {
		switch (sex) {
		case Woman:
			return "女";
		case Man:
			return "男";
		default:
			return "女";
		}
	}

	public static String statusString(Status status) {
		switch (status) {
		case YES:
			return "启用";
		case NO:
			return "停用";
		default:
			return "启用";
		}
	}
}
