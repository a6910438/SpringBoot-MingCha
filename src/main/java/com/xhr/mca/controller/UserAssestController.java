package com.xhr.mca.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.User;
import com.xhr.mca.redis.UserManagent;
import com.xhr.mca.service.UserAssestService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/user/assest")
@Log4j
public class UserAssestController {

	private final UserAssestService userAssestService;
	private final UserManagent userManagent;

	public UserAssestController(UserAssestService userAssestService, UserManagent userManagent) {
		this.userAssestService = userAssestService;
		this.userManagent = userManagent;
	}

	/**
	 * 获取我的资产列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result list() {
		try {
			User user = userManagent.getCurrentUser();
			return ResultUtil.success(userAssestService.findAssestsByUserId(user.getId()));
		} catch (WebAppException e) {
			log.error("UserAssestController list 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	@RequestMapping(value = "/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result detail(@RequestParam(value = "coin_id") Long coinID, @RequestParam(value = "type") Integer type,
			@RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer rows) {
		try {
			User user = userManagent.getCurrentUser();
			return ResultUtil.success(
					userAssestService.findTransactionsByUserIdAndCoinId(user.getId(), coinID, type, page, rows));
		} catch (WebAppException e) {
			log.error("UserAssestController detail 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

}
