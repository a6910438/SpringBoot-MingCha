package com.xhr.mca.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Order;

public interface OrderService {

	public List<Order> selectAll(Order order) throws WebAppException;

	public Order insert(Order order) throws WebAppException;

	public void update(Order order) throws WebAppException;

	public Order payspu(Order order) throws WebAppException;

	public Order findOrderByCondition(Order order) throws WebAppException;

	public BigDecimal sumOrdersAmountByUserId(Long userId) throws WebAppException;

	public void sureReceive(String orderSn, Long userId, Long pid) throws WebAppException;

	public void cancel(Order order) throws WebAppException;

	public void delete(String orderSn) throws WebAppException;

	public Order detail(String orderSn) throws WebAppException;

	public void success(String orderSn) throws WebAppException;

	public void fail(String orderSn) throws WebAppException;

	public Order getUnifiedOrder(String orderSn) throws UnsupportedEncodingException, Exception;

	public Map<String, Object> wechatPay(HttpServletRequest request, String orderSn) throws UnsupportedEncodingException, Exception, WebAppException;
}
