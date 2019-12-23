package com.xhr.mca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Config;
import com.xhr.mca.entity.constant.ConfigKeys;
import com.xhr.mca.service.ConfigService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/config")
@Log4j
public class ConfigController {

	private final ConfigService configService;

	@Autowired
	public ConfigController(ConfigService configService) {
		this.configService = configService;
	}

	@RequestMapping(value = "/android/version", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result androidVersion() {
		try {
			Config c = configService.findConfigByKey(ConfigKeys.ANDROID_CLIENT_VERSION.getKey());
			return ResultUtil.success(c);
		} catch (WebAppException e) {
			log.error("ConfigController androidVersion 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	@RequestMapping(value = "/regions", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result region(@RequestParam(value = "id") Long id) {
		try {
			return ResultUtil.success(configService.findRegionsByParentId(id));
		} catch (WebAppException e) {
			log.error("ConfigController region 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	@RequestMapping(value = "/android/download", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result downloadAndroid() {
		try {
			return ResultUtil.success(configService.findAndoridDownload());
		} catch (WebAppException e) {
			log.error("ConfigController downloadAndroid 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

}
