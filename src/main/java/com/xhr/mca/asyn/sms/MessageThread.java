package com.xhr.mca.asyn.sms;

import com.xhr.mca.config.HttpSender;
import com.xhr.mca.entity.constant.SmsTemplate;

import lombok.extern.log4j.Log4j;

@Log4j
public class MessageThread extends Thread {

	private String area;
	private String phone;
	private int length;
	private SmsTemplate template;
	private HttpSender httpSender;

	/**
	 * 
	 * @param area       区号
	 * @param phone      手机号
	 * @param length     长度
	 * @param template   模板
	 * @param httpSender http请求
	 */
	public MessageThread(String area, String phone, int length, SmsTemplate template, HttpSender httpSender) {
		super();
		this.area = area;
		this.phone = phone;
		this.length = length;
		this.template = template;
		this.httpSender = httpSender;
	}

	@Override
	public void run() {
		if (httpSender != null) {
			// 发送短信
			try {
				httpSender.sendSms(area, phone, length, template);
			} catch (Exception e) {
				log.error("MessageThread run 发送异常:", e);
			}
		}
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public SmsTemplate getTemplate() {
		return template;
	}

	public void setTemplate(SmsTemplate template) {
		this.template = template;
	}

	public HttpSender getHttpSender() {
		return httpSender;
	}

	public void setHttpSender(HttpSender httpSender) {
		this.httpSender = httpSender;
	}

}
