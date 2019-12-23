package com.xhr.mca.common;

import java.io.Serializable;

/**
 * 返回对象
 * 
 * @author xhr
 *
 */
public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2026949238965593936L;

	/** 错误码 **/
	private Integer code;
	/** 具体的内容 **/
	private Object data;
	/** 错误内容 **/
	private String msg;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
