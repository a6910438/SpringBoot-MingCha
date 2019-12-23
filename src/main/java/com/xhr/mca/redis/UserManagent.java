package com.xhr.mca.redis;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xhr.mca.common.Constants;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.constant.ExceptionConstants;
import com.xhr.mca.service.UserService;

/**
 * 获取当前登录的用户
 * 
 * @author xhr
 *
 */
@Component
public class UserManagent {

	private final RedisCache<String, Long> redis;
	private UserService userService;

	@Autowired
	public UserManagent(RedisCache<String, Long> redis, UserService userService) {
		this.redis = redis;
		this.userService = userService;
	}

	/**
	 * 根据Token获取用户
	 * 
	 * @param token
	 * @return
	 * @throws WebAppException
	 */
	public User getLoginUser(String token) throws WebAppException {
		if (redis.get(token) == null) {
			throw new WebAppException(ExceptionConstants.TOKEN_NOT_IS_EXITS);
		}
		long userID = redis.get(token) == null ? 0L : Long.parseLong(redis.get(token).toString());
		User user = userService.getUserById(userID);
		if (user == null) {
			throw new WebAppException(ExceptionConstants.USER_NOT_EXIST);
		}
		return user;
	}

	public User getCurrentUser() throws WebAppException {
		String token = getCurrentRequest().getHeader(Constants.TOKEN);
		return getLoginUser(token);
	}

	public HttpServletRequest getCurrentRequest() throws WebAppException {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

}
