package com.xhr.mca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.mca.common.Constants;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.config.HttpSender;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.constant.ExceptionConstants;
import com.xhr.mca.entity.constant.SmsTemplate;
import com.xhr.mca.mapper.UserMapper;
import com.xhr.mca.redis.RedisCache;
import com.xhr.mca.service.SmsService;

@Service
public class SmsServiceImpl implements SmsService {

	private final HttpSender httpSender;
	private final RedisCache<String, Integer> redis;
	private final UserMapper userMapper;

	@Autowired
	public SmsServiceImpl(HttpSender httpSender, RedisCache<String, Integer> redis, UserMapper userMapper) {
		this.httpSender = httpSender;
		this.redis = redis;
		this.userMapper = userMapper;
	}

	@Override
	public void sendRegisterCode(String area, String phone) throws Exception {
		// 手机号存在
		if (userMapper.selectOne(new User(phone)) != null) {
			throw new WebAppException(ExceptionConstants.USER_IS_EXIST);
		}
		// 发送验证码并返回验证码
		int code = httpSender.sendSms(area, phone, 6, SmsTemplate.REGISTER_CAPTCHA_CODE);
		// 放入到redis缓存中并设置过期时长
		redis.set(area + phone + Constants.REGIST_SUFFIX, code, Long.valueOf(Constants.FIFTEEN * Constants.MINUTE));
	}

	@Override
	public void sendForgotCode(String area, String phone) throws Exception {
		// 手机号不存在
		if (userMapper.selectOne(new User(phone)) == null) {
			throw new WebAppException(ExceptionConstants.USER_NOT_EXIST);
		}

		// 发送验证码并返回验证码
		int code = httpSender.sendSms(area, phone, 6, SmsTemplate.FORGOT_CAPTCHA_CODE);
		// 放入到redis缓存中并设置过期时长
		redis.set(area + phone + Constants.FORGOT_SUFFIX, code, Long.valueOf(Constants.FIFTEEN * Constants.MINUTE));
	}

	@Override
	public void sendUptpassCode(String area, String phone) throws Exception {
		// 手机号不存在
		if (userMapper.selectOne(new User(phone)) == null) {
			throw new WebAppException(ExceptionConstants.USER_NOT_EXIST);
		}

		// 发送验证码并返回验证码
		int code = httpSender.sendSms(area, phone, 6, SmsTemplate.UPDATE_CAPTCHA_CODE);
		// 放入到redis缓存中并设置过期时长
		redis.set(area + phone + Constants.UPTPASS_SUFFIX, code, Long.valueOf(Constants.FIFTEEN * Constants.MINUTE));
	}

	@Override
	public void sendPaypassCode(String area, String phone) throws Exception {
		// 手机号不存在
		if (userMapper.selectOne(new User(phone)) == null) {
			throw new WebAppException(ExceptionConstants.USER_NOT_EXIST);
		}

		// 发送验证码并返回验证码
		int code = httpSender.sendSms(area, phone, 6, SmsTemplate.UPDATE_PAY_CAPTCHA_CODE);
		// 放入到redis缓存中并设置过期时长
		redis.set(area + phone + Constants.PAYPASS_SUFFIX, code, Long.valueOf(Constants.FIFTEEN * Constants.MINUTE));
	}

	@Override
	public void sendWithdrawCode(String area, String phone) throws Exception {
		// 手机号不存在
		if (userMapper.selectOne(new User(phone)) == null) {
			throw new WebAppException(ExceptionConstants.USER_NOT_EXIST);
		}

		// 发送验证码并返回验证码
		int code = httpSender.sendSms(area, phone, 6, SmsTemplate.SEND_WITHDRAW_CAPTCHA_CODE);
		// 放入到redis缓存中并设置过期时长
		redis.set(area + phone + Constants.WITHDRAW_SUFFIX, code, Long.valueOf(Constants.FIFTEEN * Constants.MINUTE));
	}

}
