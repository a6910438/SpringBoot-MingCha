package com.xhr.mca.common;

import com.xhr.mca.entity.constant.ExceptionConstants;

/**
 * 返回方法组建
 * 
 * @author xhr
 *
 */
public class ResultUtil {

	/**
	 * json转为result
	 * 
	 * @param json
	 * @return
	 */
	public static Result toResultByJson(String json) {
		Result result = new Result();
		Message message = JsonUtils.jsonToPojo(json, Message.class);
		result.setCode(message.getCode());
		if (Utility.isNotNull(message.getData())) {
			result.setData(message.getData());
		}
		result.setMsg(message.getMsg());
		return result;
	}

	/**
	 * 返回成功
	 * 
	 * @param body
	 * @return
	 */
	public static Result success(Object body) {
		Result result = new Result();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setData(body);
		result.setMsg(ResultCode.SUCCESS.getMsg());
		return result;
	}

	/**
	 * 返回异常
	 * 
	 * @param e
	 * @return
	 */
	public static Result fail(ExceptionConstants e) {
		Result result = new Result();
		result.setCode(e.getErrorCode());
		result.setData(new Object());
		result.setMsg(e.getErrorMessage());
		return result;
	}

	/**
	 * 返回系统异常
	 * 
	 * @return
	 */
	public static Result fail() {
		Result result = new Result();
		result.setCode(ExceptionConstants.SERVER_EXPECTION.getErrorCode());
		result.setData(new Object());
		result.setMsg(ExceptionConstants.SERVER_EXPECTION.getErrorMessage());
		return result;
	}

	public static Result success() {
		return success(null);
	}
}
