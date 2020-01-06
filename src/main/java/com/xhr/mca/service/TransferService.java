package com.xhr.mca.service;

import java.math.BigDecimal;

import com.xhr.mca.common.WebAppException;

public interface TransferService {

	public boolean sendTransaction(long coinId, long userId, String to, BigDecimal number, String payPassword, String code) throws WebAppException, Exception;

	public void sendSystemTransaction(long coinId, long userId, String String, BigDecimal number, String payPassword, String code) throws WebAppException, Exception;
}
