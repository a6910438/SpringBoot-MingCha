package com.xhr.mca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.UserAddress;
import com.xhr.mca.redis.UserManagent;
import com.xhr.mca.service.UserAddressService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/user/address")
@Log4j
public class UserAddressController {

	private final UserAddressService userAddressService;
	private final UserManagent userManagent;

	@Autowired
	public UserAddressController(UserAddressService userAddressService, UserManagent userManagent) {
		this.userAddressService = userAddressService;
		this.userManagent = userManagent;
	}

	/**
	 * 个人收货地址列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result list() {
		try {
			User user = userManagent.getCurrentUser();
			return ResultUtil.success(userAddressService.findAllByUserId(user.getId()));
		} catch (WebAppException e) {
			log.error("UserAddressController list 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	/**
	 * 个人收货地址明细
	 * 
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result detail(@RequestParam(value = "address_id") Long addressId) {
		try {
			return ResultUtil.success(userAddressService.findAddressById(addressId));
		} catch (WebAppException e) {
			log.error("UserAddressController detail 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	/**
	 * 删除收货地址
	 * 
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result delete(@RequestParam(value = "address_id") Long addressId) {
		try {
			userAddressService.delete(addressId);
		} catch (WebAppException e) {
			log.error("UserAddressController detail 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}

	/**
	 * 添加收货地址
	 * 
	 * @param to
	 * @param phone
	 * @param area
	 * @param address
	 * @param is_default
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result add(@RequestParam(value = "to") String to, @RequestParam(value = "phone") String phone,
			@RequestParam(value = "area") String area, @RequestParam(value = "address") String address,
			@RequestParam(value = "is_default") Integer isDefault) {
		try {
			User user = userManagent.getCurrentUser();
			userAddressService.insert(new UserAddress(user.getId(), to, isDefault, phone, area, address));
		} catch (WebAppException e) {
			log.error("UserAddressController add 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}

	/**
	 * 修改收货地址
	 * 
	 * @param to
	 * @param phone
	 * @param area
	 * @param address
	 * @param is_default
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result update(@RequestParam(value = "to") String to, @RequestParam(value = "phone") String phone,
			@RequestParam(value = "area") String area, @RequestParam(value = "address") String address,
			@RequestParam(value = "is_default") Integer isDefault, @RequestParam(value = "address_id") Long addressId) {
		try {
			userAddressService.update(new UserAddress(null, addressId, to, isDefault, phone, area, address));
		} catch (WebAppException e) {
			log.error("UserAddressController add 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}
}
