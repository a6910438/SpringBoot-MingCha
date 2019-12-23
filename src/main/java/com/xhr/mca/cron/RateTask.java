package com.xhr.mca.cron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.xhr.mca.common.Constants;
import com.xhr.mca.common.Utility;
import com.xhr.mca.entity.Coin;
import com.xhr.mca.entity.vo.SunKline;
import com.xhr.mca.http.HttpSender;
import com.xhr.mca.service.CoinService;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class RateTask {

	private final HttpSender httpSender;
	private final CoinService coinService;

	@Autowired
	public RateTask(HttpSender httpSender, CoinService coinService) {
		this.httpSender = httpSender;
		this.coinService = coinService;
	}

	@Scheduled(cron = "0 */5 * * * ?")
	public void execute() {
		/**
		 * 抓取雷盾交易所提供的交易行情数据
		 */
		try {
			String json = httpSender.doGet(httpSender.getLoexUrl());
			JSONObject jo = JSONObject.parseObject(json);
			if (Utility.isNotNull(jo)) {
				JSONObject jsonObj = jo.getJSONObject("data");
				if (Utility.isNotNull(jsonObj)) {
					SunKline kline = JSONObject.toJavaObject(jsonObj, SunKline.class);
					if (Utility.isNotNull(kline)) {
						Coin coin = coinService.getCoinByName(Constants.SUN);
						coin.setRate(kline.getLast());
						coinService.update(coin);
					}
				}
			}
		} catch (Exception e) {
			log.error("RateTask execute 发送异常:", e);
		}
	}

}
