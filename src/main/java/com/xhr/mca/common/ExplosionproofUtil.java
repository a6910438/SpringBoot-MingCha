package com.xhr.mca.common;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
/**
 * 防爆击工具类
 * @author Administrator
 *
 */
public class ExplosionproofUtil {
	private static final Log log = LogFactory.getLog(ExplosionproofUtil.class);
	/**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
	 * 
	 * @return ip
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		//log.info("x-forwarded-for ip: " + ip);
		if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			if (ip.indexOf(",") != -1) {
				ip = ip.split(",")[0];
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			//log.info("Proxy-Client-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			//log.info("WL-Proxy-Client-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
			//log.info("HTTP_CLIENT_IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			//log.info("HTTP_X_FORWARDED_FOR ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
			//log.info("X-Real-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			//log.info("getRemoteAddr ip: " + ip);
		}
		return ip;
	}
	/**
	  * 判断是不是爆击请求
	 * @param request
	 * @param redisTemplate
	 * @return true 是爆击 ，false 不是爆击
	 */
	public static boolean isExplosionproof(HttpServletRequest request, ValueOperations<String, String> valOpsStr) {
		String ip=getIpAddr(request);
		log.info("请求IP:"+ip);
		if(StringUtils.isEmpty(ip)) {
			return false;
		}
		String sign=request.getParameter("sign");
		if(StringUtils.isEmpty(sign)) {
			sign=request.getServletPath();
		}
		if("/error".equals(sign)) {
			return false;
		}
		String key=ip+":"+sign+"_"+request.getServletPath();
		log.info("请求》》"+key);
		String value=valOpsStr.get(key);
		if(StringUtils.isEmpty(value)) {
			valOpsStr.set(key, "1",5, TimeUnit.SECONDS);
			return false;
		}
		int count=Integer.parseInt(value);
		if(count>5) {
			return true;
		}
		log.info(key+"_"+request.getServletPath());
		count++;
		valOpsStr.set(key, count+"",5, TimeUnit.SECONDS);
		return false;
	}
}
