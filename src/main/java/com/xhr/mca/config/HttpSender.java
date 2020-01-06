package com.xhr.mca.config;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xhr.mca.common.Utility;
import com.xhr.mca.entity.constant.SmsTemplate;

import lombok.Data;
import lombok.extern.log4j.Log4j;

@Component
@Log4j
@Data
public class HttpSender {

	// 编码格式
	private static final String CHARSET = "UTF-8";

	@Value("${sms.url}")
	private String smsUrl;
	@Value("${sms.account}")
	private String smsAccount;
	@Value("${sms.password}")
	private String smsPassword;
	@Value("${loex.openapi.url}")
	private String loexUrl;

	/***
	 * 
	 * @param areaCode
	 * @param phone
	 * @param length
	 * @param type     1注册，2找回密码，3修改/重置密码，4修改支付密码，5注册成功通知，6修改密码成功通知，7支付密码设置成功通知
	 * @return
	 * @throws Exception
	 */
	public int sendSms(String area, String phone, int length, SmsTemplate template) throws Exception {
		int minute = 3;
		String msg = null;

		// 根据长度获取随机数
		int code = Utility.getRandomNumber(length);

		// 解析发送短信的模板
		switch (template) {
		case REGISTER_CAPTCHA_CODE:
			msg = String.format(SmsTemplate.REGISTER_CAPTCHA_CODE.getMessage(), code, minute);
			break;
		case UPDATE_CAPTCHA_CODE:
			msg = String.format(SmsTemplate.UPDATE_CAPTCHA_CODE.getMessage(), code, minute);
			break;
		case UPDATE_PAY_CAPTCHA_CODE:
			msg = String.format(SmsTemplate.UPDATE_PAY_CAPTCHA_CODE.getMessage(), code, minute);
			break;
		case CONGRATULATIONS_REGISTER:
			msg = String.format(SmsTemplate.CONGRATULATIONS_REGISTER.getMessage(), code, minute);
			break;
		case LOGIN_PASSWORD_UPDATE_SUCCESS:
			msg = String.format(SmsTemplate.LOGIN_PASSWORD_UPDATE_SUCCESS.getMessage(), minute);
			break;
		case PAY_PASSWORD_UPDATE_SUCCESS:
			msg = String.format(SmsTemplate.PAY_PASSWORD_UPDATE_SUCCESS.getMessage(), minute);
			break;
		case FORGOT_CAPTCHA_CODE:
			msg = String.format(SmsTemplate.FORGOT_CAPTCHA_CODE.getMessage(), code, minute);
			break;
		case SEND_WITHDRAW_CAPTCHA_CODE:
			msg = String.format(SmsTemplate.SEND_WITHDRAW_CAPTCHA_CODE.getMessage(), code, minute);
			break;
		default:
			msg = "";
		}

		// 开始发送
		send(area, phone, msg);
		return code;
	}

	/**
	 * 发送短信
	 * 
	 * @param area
	 * @param phone
	 * @param msg
	 * @throws Exception
	 */
	public void send(String area, String phone, String msg) throws Exception {
		// 拼接下手机号
		String mobile = area + phone;
		if (phone.startsWith(area + "")) {
			mobile = phone;
		}
		// 拼接发送参数
		JSONObject map = new JSONObject();
		map.put("account", smsAccount);
		map.put("password", smsPassword);
		map.put("msg", msg);
		map.put("mobile", mobile);

		String params = map.toString();
		log.info("请求参数为:" + params);

		// 参数转成json发送出去
		String result = doPost(smsUrl, params);
		log.info("返回参数为:" + result);
		JSONObject jsonObject = JSON.parseObject(result);
		String resCode = jsonObject.get("code").toString();
		String msgid = jsonObject.get("msgid").toString();
		String error = jsonObject.get("error").toString();
		log.info("状态码:" + resCode + ",状态码说明:" + error + ",消息id:" + msgid);

	}

	/**
	 * Post请求接口
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String doPost(String url, String params) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		StringEntity sEntity = new StringEntity(params, CHARSET);
		httpPost.setEntity(sEntity);
		CloseableHttpResponse response = httpClient.execute(httpPost);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, CHARSET);
			}
		} finally {
			response.close();
			httpClient.close();
		}
		return null;
	}

	public String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			URI uri = builder.build();
			HttpGet httpGet = new HttpGet(uri);

			if (param != null) {
				for (String key : param.keySet()) {
					if ("token".equals(key)) {
						httpGet.addHeader("Authorization", param.get(key));
					}
					if ("eosex-source".equals(key)) {
						httpGet.addHeader("eosex-source", param.get(key));
					}
					builder.addParameter(key, param.get(key));
				}
			}
			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}

	public String doGet(String url) {
		return doGet(url, null);
	}

}
