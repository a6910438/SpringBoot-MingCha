package com.xhr.mca.auth;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.xhr.mca.common.Constants;
import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.entity.constant.ExceptionConstants;
import com.xhr.mca.redis.RedisCache;

public class SecurityInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		String token = request.getHeader(Constants.TOKEN);
		BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
		RedisCache<String, Long> redis = (RedisCache<String, Long>) factory.getBean("redis");
		long userID = redis.get(token) == null ? 0L : Long.parseLong(redis.get(token).toString());
		if (userID != 0) {
			return super.preHandle(request, response, handler);
		}
		Result result = ResultUtil.fail(ExceptionConstants.TOKEN_NOT_IS_EXITS);
		PrintWriter out = null;
		try {
			response.setContentType(Constants.CONTENT_TYPE);
			out = response.getWriter();
			out.write(JSON.toJSONString(result));
			out.flush();
		} catch (Exception e) {
			if (out != null) {
				out.close();
			}
		}

		return false;
	}

}
