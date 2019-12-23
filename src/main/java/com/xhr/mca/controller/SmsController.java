package com.xhr.mca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.service.SmsService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/sms")
@Log4j
public class SmsController {

	private final SmsService smsService;

	@Autowired
	public SmsController(SmsService smsService) {
		this.smsService = smsService;
	}

	@RequestMapping(value = "/send_register_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result sendRegisterCode(@RequestParam(value = "areaCode") String area,
			@RequestParam(value = "phone") String phone) {
		try {
			smsService.sendRegisterCode(area, phone);
		} catch (Exception e) {
			log.error("SmsController sendRegisterCode 发生异常:", e);
			return ResultUtil.fail();
		}
		return ResultUtil.success();
	}

	@RequestMapping(value = "/send_forgot_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result sendForgotCode(@RequestParam(value = "areaCode") String area,
			@RequestParam(value = "phone") String phone) {
		try {
			smsService.sendForgotCode(area, phone);
		} catch (Exception e) {
			log.error("SmsController sendRegisterCode 发生异常:", e);
			return ResultUtil.fail();
		}
		return ResultUtil.success();
	}

	@RequestMapping(value = "/send_uptpass_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result sendUptpassCode(@RequestParam(value = "areaCode") String area,
			@RequestParam(value = "phone") String phone) {
		try {
			smsService.sendUptpassCode(area, phone);
			return ResultUtil.success();
		} catch (Exception e) {
			log.error("SmsController sendRegisterCode 发生异常:", e);
			return ResultUtil.fail();
		}
	}

	@RequestMapping(value = "/send_paypass_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result sendPaypassCode(@RequestParam(value = "areaCode") String area,
			@RequestParam(value = "phone") String phone) {
		try {
			smsService.sendPaypassCode(area, phone);
		} catch (Exception e) {
			log.error("SmsController sendRegisterCode 发生异常:", e);
			return ResultUtil.fail();
		}
		return ResultUtil.success();
	}
}
