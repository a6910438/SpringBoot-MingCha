package com.xhr.mca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Banner;
import com.xhr.mca.entity.constant.Status;
import com.xhr.mca.service.BannerService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/banner")
@Log4j
public class BannerController {

	private final BannerService bannerService;

	@Autowired
	public BannerController(BannerService bannerService) {
		this.bannerService = bannerService;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result list() {
		try {
			return ResultUtil.success(bannerService.selectAll(new Banner(Status.YES.ordinal())));
		} catch (WebAppException e) {
			log.error("BannerController list 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

}
