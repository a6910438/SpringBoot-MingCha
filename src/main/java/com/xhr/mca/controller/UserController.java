package com.xhr.mca.controller;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xhr.mca.common.ExplosionproofUtil;
import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.constant.ExceptionConstants;
import com.xhr.mca.entity.constant.Level;
import com.xhr.mca.redis.UserManagent;
import com.xhr.mca.service.UserAuthService;
import com.xhr.mca.service.UserService;

import lombok.extern.log4j.Log4j;
import tk.mybatis.mapper.util.StringUtil;

@RestController
@RequestMapping("/user")
@Log4j
public class UserController {

	private final UserService userService;
	private final UserManagent userManagent;
	private final UserAuthService userAuthService;

	@Autowired
	public UserController(UserService userService, UserManagent userManagent, UserAuthService userAuthService) {
		this.userService = userService;
		this.userManagent = userManagent;
		this.userAuthService = userAuthService;
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param areaCode
	 * @param phone
	 * @param password1
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result login(HttpServletRequest request, @RequestParam(value = "areaCode") String areaCode,
			@RequestParam(value = "phone") String phone, @RequestParam(value = "password") String password1) {
		if (StringUtil.isEmpty(areaCode) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(password1)) {
			return ResultUtil.fail(ExceptionConstants.ERROR_PARAME);
		}

		User user = null;
		try {
			// 调用登录服务
			user = userService.login(
					new User(phone, password1, null, areaCode, null, null, ExplosionproofUtil.getIpAddr(request)));
		} catch (WebAppException e) {
			log.error("UserController login 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success(user);
	}

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param areaCode
	 * @param phone
	 * @param password1
	 * @param verificationCode
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result register(HttpServletRequest request, @RequestParam(value = "areaCode") String areaCode,
			@RequestParam(value = "phone") String phone, @RequestParam(value = "password") String password,
			@RequestParam(value = "pay_password") String payPassword,
			@RequestParam(value = "verCode") String verificationCode,
			@RequestParam(value = "inviteCode", required = false) String inviteCode) {
		if (StringUtil.isEmpty(areaCode) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(password)
				|| StringUtil.isEmpty(verificationCode)) {
			return ResultUtil.fail(ExceptionConstants.ERROR_PARAME);
		}
		try {
			// 调用注册服务
			userService.register(new User(phone, password, payPassword, areaCode, verificationCode, inviteCode,
					ExplosionproofUtil.getIpAddr(request)));
		} catch (WebAppException e) {
			log.error("UserController register 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}

	/**
	 * 忘记密码
	 * 
	 * @param request
	 * @param areaCode
	 * @param phone
	 * @param password1
	 * @param verificationCode
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/forgot/pass", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result forgotPass(@RequestParam(value = "phone") String phone,
			@RequestParam(value = "password") String password, @RequestParam(value = "areaCode") String areaCode,
			@RequestParam(value = "verificationCode") String verificationCode) {
		if (StringUtil.isEmpty(areaCode) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(password)
				|| StringUtil.isEmpty(verificationCode)) {
			return ResultUtil.fail(ExceptionConstants.ERROR_PARAME);
		}
		try {
			userService.forgot(new User(phone, password, null, areaCode, verificationCode, null, null));
		} catch (WebAppException e) {
			log.error("UserController forgotPass 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}

	/**
	 * 修改个人信息
	 * 
	 * @param request
	 * @param sex
	 * @param nickname
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/info/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result updateInfo(@RequestParam(value = "sex", required = false) Integer sex,
			@RequestParam(value = "nickname", required = false) String nickname,
			@RequestParam(value = "email", required = false) String email) {
		try {
			User user = userManagent.getCurrentUser();
			user.setSex(sex);
			user.setNickname(nickname);
			user.setEmail(email);
			userService.update(user);
		} catch (IOException e) {
			log.error("UserController updateInfo 上传异常:", e);
		} catch (WebAppException e) {
			log.error("UserController updateInfo 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}

	/**
	 * 上传头像
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/info/update/image", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result uploadImage(@RequestParam(value = "file", required = false) String base64FileString) {
		try {
			User user = userManagent.getCurrentUser();
			user.setBase64FileString(base64FileString);
			userService.update(user);
		} catch (IOException e) {
			log.error("UserController updateInfo 上传异常:", e);
		} catch (WebAppException e) {
			log.error("UserController updateInfo 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}

	/**
	 * 获取个人信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result info() {
		try {
			User u = userManagent.getCurrentUser();
			u.setIs_pass(userAuthService.getStatusByUserId(u.getId()).ordinal());
			u.setPassword1("");
			u.setPassword2("");
			u.setLevelString(User.levelString(Level.values()[u.getLevel()]));
			return ResultUtil.success(u);
		} catch (WebAppException e) {
			log.error("UserController info 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	/**
	 * 修改登录密码
	 * 
	 * @param verCode
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/login/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result updateLoginPass(@RequestParam(value = "verCode", required = false) String verCode,
			@RequestParam(value = "password", required = false) String password) {
		try {
			User user = userManagent.getCurrentUser();
			user.setVerCode(verCode);
			user.setPassword1(password);
			userService.updateLoginPass(user);
		} catch (WebAppException e) {
			log.error("UserController updateLoginPass 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}

	/**
	 * 修改支付密码
	 * 
	 * @param request
	 * @param sex
	 * @param nickname
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/pay/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result updatePayPass(@RequestParam(value = "verCode", required = false) String verCode,
			@RequestParam(value = "password", required = false) String password) {
		try {
			User user = userManagent.getCurrentUser();
			user.setVerCode(verCode);
			user.setPassword2(password);
			userService.updatePayPass(user);
		} catch (WebAppException e) {
			log.error("UserController updatePayPass 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}
}
