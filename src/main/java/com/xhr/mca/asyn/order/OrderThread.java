package com.xhr.mca.asyn.order;

import java.math.BigDecimal;

import com.xhr.mca.entity.Config;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.constant.ConfigKeys;
import com.xhr.mca.entity.constant.Level;
import com.xhr.mca.service.ConfigService;
import com.xhr.mca.service.OrderService;
import com.xhr.mca.service.UserService;

import lombok.extern.log4j.Log4j;

@Log4j
public class OrderThread extends Thread {

	private Long userId;
	private UserService userService;
	private OrderService orderService;
	private ConfigService configService;

	public OrderThread(UserService userService, OrderService orderService, Long userId, ConfigService configService) {
		this.userId = userId;
		this.userService = userService;
		this.orderService = orderService;
		this.configService = configService;
	}

	@Override
	public void run() {
		try {
			Config goldConfig = configService.findConfigByKey(ConfigKeys.GOLD_USER_PAY_AMOUNT.getKey());
			Config partnetConfig = configService.findConfigByKey(ConfigKeys.PARTNET_USER_PAY_AMOUNT.getKey());
			if (goldConfig != null && partnetConfig != null) {
				BigDecimal sumAmounts = orderService.sumOrdersAmountByUserId(userId);
				if (sumAmounts.compareTo(new BigDecimal(partnetConfig.getValue())) >= 0) {
					User u = new User();
					u.setId(userId);
					u.setLevel(Level.Partnet.ordinal());
					userService.update(u);
				} else if (sumAmounts.compareTo(new BigDecimal(goldConfig.getValue())) >= 0) {
					User u = new User();
					u.setId(userId);
					u.setLevel(Level.Partnet.ordinal());
					userService.update(u);
				}
			}
		} catch (Exception e) {
			log.error("OrderThread run error:", e);
		}
	}

}
