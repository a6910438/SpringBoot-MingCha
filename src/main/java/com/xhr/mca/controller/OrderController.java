package com.xhr.mca.controller;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xhr.mca.common.Result;
import com.xhr.mca.common.ResultUtil;
import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Order;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.constant.OrderStatus;
import com.xhr.mca.redis.UserManagent;
import com.xhr.mca.service.OrderService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/order")
@Log4j
public class OrderController {

	private final OrderService orderService;
	private final UserManagent userManager;

	public OrderController(OrderService orderService, UserManagent userManager) {
		this.orderService = orderService;
		this.userManager = userManager;
	}

	/**
	 * 查询我的订单
	 * 
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result list(@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer rows) {
		try {
			User user = userManager.getCurrentUser();
			PageHelper.startPage(page, rows);
			List<Order> orders = orderService.selectAll(new Order(user.getId(), status));
			PageInfo<Order> pageInfo = new PageInfo<Order>(orders);
			return ResultUtil.success(pageInfo);
		} catch (WebAppException e) {
			log.error("OrderController list 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	/**
	 * 取消订单
	 * 
	 * @param sn
	 * @param reason
	 * @return
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result update(@RequestParam(value = "order_sn") String sn, @RequestParam(value = "reason") String reason) {
		try {
			orderService.cancel(new Order(sn, OrderStatus.CANCEL.ordinal(), reason));
		} catch (WebAppException e) {
			log.error("OrderController cancel 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}

	/**
	 * 添加未付款的订单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result pay(@RequestParam(value = "product_sn") String sn, @RequestParam(value = "address_id") Long addressId,
			@RequestParam(value = "number") Integer number,
			@RequestParam(value = "note", required = false) String note) {
		try {
			User user = userManager.getCurrentUser();
			return ResultUtil.success(orderService.insert(new Order(user.getId(), sn, addressId, number, note)));
		} catch (WebAppException e) {
			log.error("OrderController pay 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	/**
	 * 用SPU支付时接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/spu", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result paySpu(@RequestParam(value = "order_sn") String sn,
			@RequestParam(value = "pay_pass") String payPass) {
		try {
			User user = userManager.getCurrentUser();
			return ResultUtil.success(orderService.payspu(new Order(sn, user.getId(), payPass)));
		} catch (WebAppException e) {
			log.error("OrderController paySpu 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	/**
	 * 确认收货
	 * 
	 * @param sn
	 * @return
	 */
	@RequestMapping(value = "/sure/rece", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result sureReceive(@RequestParam(value = "order_sn") String sn) {
		try {
			User user = userManager.getCurrentUser();
			orderService.sureReceive(sn, user.getId(), user.getPid());
		} catch (WebAppException e) {
			log.error("OrderController sureReceive 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
		return ResultUtil.success();
	}

	/**
	 * 查看订单明细
	 * 
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result detail(@RequestParam(value = "order_sn") String orderSn) {
		try {
			return ResultUtil.success(orderService.detail(orderSn));
		} catch (WebAppException e) {
			log.error("OrderController detail 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	/**
	 * 删除订单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result delete(@RequestParam(value = "order_sn") String orderSn) {
		try {
			orderService.delete(orderSn);
			return ResultUtil.success();
		} catch (WebAppException e) {
			log.error("OrderController pay 发生异常:", e);
			return ResultUtil.fail(e.geteConstants());
		}
	}

	/**
	 * 微信支付接口
	 * 
	 * @param request
	 * @param orderSn
	 * @return
	 */
	@RequestMapping(value = "/wechat/pay", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Result wechatPay(HttpServletRequest request, @RequestParam(value = "order_sn") String orderSn) {
		try {
			return ResultUtil.success(orderService.wechatPay(request, orderSn));
		} catch (Exception e) {
			log.error("OrderController generateSignature 发生异常", e);
			return ResultUtil.fail();
		}
	}

	/**
	 * 支付回调订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/payCallback", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void payCallback(HttpServletRequest request, HttpServletResponse response) {
		try {
			String inputLine = "";
			String notityXml = "";
			while ((inputLine = request.getReader().readLine()) != null) {
				notityXml += inputLine;
			}
			// 关闭流
			request.getReader().close();
			log.info("微信回调内容信息：" + notityXml);
			// 解析成Map
			Map<String, String> map = Utility.doXMLParse(notityXml);
			// 判断 支付是否成功
			if ("SUCCESS".equals(map.get("result_code"))) {
				// 获得 返回的商户订单号
				String id = map.get("out_trade_no");
				log.info("微信回调返回商户订单号：" + id);
				// 支付code
				String transaction_id = map.get("transaction_id");
				// 访问DB
				log.info("微信回调 根据订单号查询订单状态：" + transaction_id);
				// 做业务处理, 由于需要userId 另写一个 前端支付成功 接口 做处理
				orderService.success(id);
				// 判断 是否更新成功
				log.info("微信回调  订单号：" + id + ",修改状态成功");
				// 封装 返回值
				StringBuffer buffer = new StringBuffer();
				buffer.append("<xml>");
				buffer.append("<return_code>SUCCESS</return_code>");
				buffer.append("<return_msg>OK</return_msg>");
				buffer.append("</xml>");
				// 给微信服务器返回 成功标示 否则会一直询问 咱们服务器 是否回调成功
				PrintWriter writer = response.getWriter();
				// 返回
				writer.print(buffer.toString());
			} else {
				// 回滚库存
				orderService.fail(map.get("out_trade_no"));
			}
		} catch (WebAppException e) {
			log.error("OrderController payCallback 发生异常:", e);
		} catch (Exception e) {
			log.error("OrderController payCallback 发生异常:", e);
		}
	}

}
