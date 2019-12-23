package com.xhr.mca.auth;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SecurityInterceptor())
				.addPathPatterns("/user/**", "/sms/**", "/banner/**", "/product/**", "/order/**")
				.excludePathPatterns("/user/login").excludePathPatterns("/user/register")
				.excludePathPatterns("/user/forgot/pass").excludePathPatterns("/sms/send_register_code")
				.excludePathPatterns("/sms/send_forgot_code").excludePathPatterns("/config/android/version").excludePathPatterns("/order/payCallback");
		super.addInterceptors(registry);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		converter.setFeatures(SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty, // 字符串null返回空字符串
				SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.PrettyFormat);
		converters.add(converter);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowCredentials(true).allowedOrigins("*")
				.allowedMethods("GET", "POST", "DELETE", "PUT").maxAge(3600);
	}

}
