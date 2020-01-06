package com.xhr.mca.service;

public interface SmsService {

	public void sendRegisterCode(String area, String phone) throws Exception;

	public void sendForgotCode(String area, String phone) throws Exception;

	public void sendUptpassCode(String area, String phone) throws Exception;

	public void sendPaypassCode(String area, String phone) throws Exception;

	public void sendWithdrawCode(String area, String phone) throws Exception;

}
