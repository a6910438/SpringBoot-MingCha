package com.xhr.mca.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.constant.ExceptionConstants;
import com.xhr.mca.redis.UserManagent;
import com.xhr.mca.service.TransferService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/transfer")
@Log4j
public class TransferController {

	private final TransferService transferService;
	private final UserManagent userManagent;

	@Autowired
	public TransferController(TransferService transferService, UserManagent userManagent) {
		this.transferService = transferService;
		this.userManagent = userManagent;
	}

	/**
	 * 提币
	 * 
	 * @param coinId
	 * @param userId
	 * @param address
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result withdraw(@RequestParam(value = "coin_id") Long coinId, @RequestParam(value = "to") String to,
			@RequestParam(value = "number") BigDecimal number, @RequestParam(value = "pay_password") String payPassword,
			@RequestParam(value = "code") String code) {
		try {
			boolean flag = transferService.sendTransaction(coinId, userManagent.getCurrentUser().getId(), to, number,
					payPassword, code);
			// 金额大于后台设置的数量 则提示用户进入后台审核状态
			if (!flag) {
				return ResultUtil.fail(ExceptionConstants.APPLY_WITHDRAW_SUCCESS);
			}
			return ResultUtil.success();
		} catch (WebAppException e) {
			log.error("TransferController withdraw 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		} catch (Exception e) {
			log.error("TransferController withdraw 发生异常:", e);
			return ResultUtil.fail();
		}
	}

	/**
	 * 提币
	 * 
	 * @param coinId
	 * @param userId
	 * @param address
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/system/transfer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result systemTransfer(@RequestParam(value = "coin_id") Long coinId,
			@RequestParam(value = "phone") String phone, @RequestParam(value = "number") BigDecimal number,
			@RequestParam(value = "pay_password") String payPassword, @RequestParam(value = "code") String code) {
		try {
			transferService.sendSystemTransaction(coinId, userManagent.getCurrentUser().getId(), phone, number,
					payPassword, code);
			return ResultUtil.success();
		} catch (WebAppException e) {
			log.error("TransferController withdraw 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		} catch (Exception e) {
			log.error("TransferController withdraw 发生异常:", e);
			return ResultUtil.fail();
		}
	}

}
