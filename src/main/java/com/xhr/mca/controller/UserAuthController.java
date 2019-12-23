package com.xhr.mca.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.UserAuth;
import com.xhr.mca.entity.constant.AuditStatus;
import com.xhr.mca.entity.constant.ExceptionConstants;
import com.xhr.mca.redis.UserManagent;
import com.xhr.mca.service.UserAuthService;

import lombok.extern.log4j.Log4j;
import tk.mybatis.mapper.util.StringUtil;

@RestController
@RequestMapping("/user")
@Log4j
public class UserAuthController {

	private final UserAuthService userAuthService;
	private final UserManagent userManagent;

	@Autowired
	public UserAuthController(UserAuthService userAuthService, UserManagent userManagent) {
		this.userAuthService = userAuthService;
		this.userManagent = userManagent;
	}

	/**
	 * 提交实名认证
	 * 
	 * @param idcardName
	 * @param idcard
	 * @param certType
	 * @param frontImgUrl base64
	 * @param faceImgUrl  base64
	 * @param backImgUrl  base64
	 * @return
	 */
	@RequestMapping(value = "/auth/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result add(@RequestParam(value = "idCardName") String idcardName,
			@RequestParam(value = "idcard") String idcard, @RequestParam(value = "sex") Integer sex,
			@RequestParam(value = "cert_type") Integer certType,
			@RequestParam(value = "front_img_file") String frontImgFile,
			@RequestParam(value = "face_img_file") String faceImgFile,
			@RequestParam(value = "back_img_file") String backImgFile,
			@RequestParam(value = "country") String country) {
		if (StringUtil.isEmpty(idcard) || StringUtil.isEmpty(idcardName) || StringUtil.isEmpty(country)
				|| Utility.isNull(frontImgFile) || Utility.isNull(faceImgFile) || Utility.isNull(backImgFile)) {
			return ResultUtil.fail(ExceptionConstants.ERROR_PARAME);
		}
		try {
			User u = userManagent.getCurrentUser();
			userAuthService.insert(new UserAuth(null, u.getId(), idcardName, idcard, sex, certType, frontImgFile,
					backImgFile, faceImgFile, AuditStatus.Audit.ordinal(), null, country, Utility.currentTimestamp()));
		} catch (IOException e) {
			log.error("UserAddressController add 阿里云上传异常:", e);
		} catch (WebAppException e) {
			log.error("UserAddressController add 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}

	/**
	 * 获取实名审核信息
	 * 
	 * @param authId
	 * @return
	 */
	@RequestMapping(value = "/auth/info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result info(@RequestParam(value = "auth_id") Long authId) {
		try {
			return ResultUtil.success(userAuthService.findUserAuthById(authId));
		} catch (WebAppException e) {
			log.error("UserAddressController list 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

}
