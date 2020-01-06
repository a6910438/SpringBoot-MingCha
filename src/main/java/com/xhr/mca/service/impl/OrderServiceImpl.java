package com.xhr.mca.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhr.mca.asyn.order.OrderThread;
import com.xhr.mca.common.Constants;
import com.xhr.mca.common.MD5;
import com.xhr.mca.common.SnowFlake;
import com.xhr.mca.common.Utility;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.BonusPool;
import com.xhr.mca.entity.BonusPoolRecord;
import com.xhr.mca.entity.Coin;
import com.xhr.mca.entity.Config;
import com.xhr.mca.entity.McaRecord;
import com.xhr.mca.entity.Order;
import com.xhr.mca.entity.Product;
import com.xhr.mca.entity.RmbRecord;
import com.xhr.mca.entity.SunRecord;
import com.xhr.mca.entity.User;
import com.xhr.mca.entity.UserAddress;
import com.xhr.mca.entity.UserAssest;
import com.xhr.mca.entity.constant.AuthStatus;
import com.xhr.mca.entity.constant.ConfigKeys;
import com.xhr.mca.entity.constant.Direction;
import com.xhr.mca.entity.constant.ExceptionConstants;
import com.xhr.mca.entity.constant.Level;
import com.xhr.mca.entity.constant.OrderStatus;
import com.xhr.mca.entity.constant.OrderType;
import com.xhr.mca.entity.constant.PayType;
import com.xhr.mca.entity.constant.Status;
import com.xhr.mca.entity.constant.Type;
import com.xhr.mca.mapper.OrderMapper;
import com.xhr.mca.mapper.UserAddressMapper;
import com.xhr.mca.service.BonusPoolRecordService;
import com.xhr.mca.service.BonusPoolService;
import com.xhr.mca.service.CoinService;
import com.xhr.mca.service.ConfigService;
import com.xhr.mca.service.McaRecordService;
import com.xhr.mca.service.OrderService;
import com.xhr.mca.service.ProductService;
import com.xhr.mca.service.RmbRecordService;
import com.xhr.mca.service.SunRecordService;
import com.xhr.mca.service.UserAssestService;
import com.xhr.mca.service.UserAuthService;
import com.xhr.mca.service.UserService;

import lombok.extern.log4j.Log4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Log4j
public class OrderServiceImpl implements OrderService {

	private final OrderMapper orderMapper;
	private final UserAddressMapper userAddressMapper;
	private final ProductService productService;
	private final UserAssestService userAssestService;
	private final CoinService coinService;
	private final SunRecordService spuRecordService;
	private final McaRecordService mcaRecordService;
	private final UserService userService;
	private final ConfigService configService;
	private final RmbRecordService rmbRecordService;
	private final BonusPoolService bonusPoolService;
	private final BonusPoolRecordService bonusPoolRecordService;
	private final UserAuthService userAuthService;

	@Value("${wechat.appid}")
	private String APPID;
	@Value("${wechat.mcaid}")
	private String MCHID;
	@Value("${wechat.apikey}")
	private String APIKEY;
	@Value("${wechat.unifiedorder.url}")
	private String UNIFIEDORDERURL;
	@Value("${wechat.paycallback.url}")
	private String PAYCALLBACKURL;

	@Autowired
	public OrderServiceImpl(OrderMapper orderMapper, UserAddressMapper userAddressMapper, ProductService productService,
			UserAssestService userAssestService, CoinService coinService, SunRecordService spuRecordService,
			UserService userService, ConfigService configService, McaRecordService mcaRecordService,
			RmbRecordService rmbRecordService, BonusPoolService bonusPoolService,
			BonusPoolRecordService bonusPoolRecordService, UserAuthService userAuthService) {
		this.orderMapper = orderMapper;
		this.userAddressMapper = userAddressMapper;
		this.productService = productService;
		this.userAssestService = userAssestService;
		this.coinService = coinService;
		this.spuRecordService = spuRecordService;
		this.userService = userService;
		this.configService = configService;
		this.mcaRecordService = mcaRecordService;
		this.rmbRecordService = rmbRecordService;
		this.bonusPoolService = bonusPoolService;
		this.userAuthService = userAuthService;
		this.bonusPoolRecordService = bonusPoolRecordService;
	}

	@Override
	public List<Order> selectAll(Order order) throws WebAppException {
		Example example = new Example(Order.class);
		example.setOrderByClause("create_time DESC");
		Criteria c = example.createCriteria();
		if (order.getOrderStatus() != -1) {
			c.andEqualTo("orderStatus", order.getOrderStatus());
		}
		c.andEqualTo("isDelete", 0);
		c.andEqualTo("userId", order.getUserId());
		List<Order> orders = orderMapper.selectByExample(example);
		for (Order o : orders) {
			Product p = productService.findProductBySn(o.getProductSn());
			if (Utility.isNotNull(p)) {

				Coin coin = coinService.getCoinById(p.getCoinId());
				if (Utility.isNotNull(coin)) {
					o.setCoinString(coin.getName());
				}

				o.setPic(p.getImgUrl());
				o.setProductName(p.getProductName());
				o.setProductDesc(p.getDescription());
				o.setPayAmount(o.getPayAmount().setScale(2));
				o.setOrderAmount(o.getOrderAmount().setScale(2));
				if (p.getPayType() != PayType.SUN.ordinal()) {
					BigDecimal orderMoney = o.getAmount().multiply(new BigDecimal(o.getNumber()))
							.add(Utility.isZero(new BigDecimal(p.getFreight())));
					o.setOrderAmount(orderMoney);
					o.setFreight(p.getFreight());
					o.setPayAmount(orderMoney);
				}
			}

		}
		return orders;
	}

	@Override
	@Transactional
	public Order insert(Order order) throws WebAppException {
		Product p = productService.findProductBySn(order.getProductSn());
		if (Utility.isNull(p)) {
			throw new WebAppException(ExceptionConstants.PRODUCT_NOT_FOUND);
		}
		AuthStatus authStatus = userAuthService.getStatusByUserId(order.getUserId());
		if (authStatus != AuthStatus.PASS) {
			throw new WebAppException(ExceptionConstants.PASS_AUTH_ORDER);
		}
		int sumnumber = orderMapper.sumStockNumberByProductSn(p.getProductSn());
		if (p.getStock() < sumnumber + order.getNumber()) {
			throw new WebAppException(ExceptionConstants.STOCK_INSUFFICIENT);
		}

		UserAddress ua = userAddressMapper.selectByPrimaryKey(order.getAddressId());
		order.setAddressPhone(ua.getPhone());
		order.setAddressUserName(ua.getName());
		order.setUserAddress(ua.getArea() + " " + ua.getDetailAddress());
		order.setCreateTime(Utility.currentTimestamp());
		order.setUpdateTime(Utility.currentTimestamp());

		order.setOrderSn(Constants.ORDER_SN_SUFFIX + SnowFlake.getOrderSn());
		order.setIsDelete(Status.NO.ordinal());
		order.setPayType(p.getPayType());
		order.setPayStatus(Status.NO.ordinal());
		order.setOrderType(OrderType.COMMON.ordinal());
		order.setOrderStatus(OrderStatus.PENDING_PAY.ordinal());
		order.setConfirmStatus(Status.NO.ordinal());

		BigDecimal freight = new BigDecimal(p.getFreight());

		if (p.getPayType() == PayType.SUN.ordinal()) {
			// 数字货币 不加运费
			order.setAmount(p.getCoinPrice());
			order.setOrderAmount(order.getAmount().multiply(new BigDecimal(order.getNumber())));
			order.setPayAmount(order.getAmount().multiply(new BigDecimal(order.getNumber())));
			order.setCoinId(p.getCoinId());
		} else {
			// 人民币
			order.setAmount(p.getPrice());
			order.setOrderAmount(order.getAmount().multiply(new BigDecimal(order.getNumber())));
			order.setPayAmount(
					order.getAmount().multiply(new BigDecimal(order.getNumber())).add(Utility.isZero(freight)));
		}
		Coin coin = coinService.getCoinById(p.getCoinId());
		order.setCoinString(coin.getName());
		order.setCoinImgUrl(coin.getIconUrl());

		orderMapper.insert(order);
		return order;
	}

	@Override
	@Transactional
	public void update(Order order) throws WebAppException {
		orderMapper.updateByPrimaryKeySelective(order);
	}

	@Override
	@Transactional
	public Order payspu(Order order) throws WebAppException {
		// 判断订单是否存在
		order.setIsDelete(Status.NO.ordinal());
		order.setPayStatus(Status.NO.ordinal());
		order.setOrderStatus(OrderStatus.PENDING_PAY.ordinal());
		Order o = findOrderByCondition(order);
		if (Utility.isNull(o)) {
			throw new WebAppException(ExceptionConstants.ORDER_NOT_FOUND);
		}

		userService.checkPayPassword(order.getUserId(), order.getPayPassword());

		Coin coin = coinService.getCoinByName(Constants.SUN);
		UserAssest ua = userAssestService.findAssestByUserIdAndCoinId(order.getUserId(), coin.getId());

		if (ua.getAvaBalance().compareTo(o.getPayAmount()) < 0) {
			throw new WebAppException(ExceptionConstants.INSUFFICIENT_FUNDS);
		}
		// 修改余额 减去订单金额
		BigDecimal old = ua.getAvaBalance();
		ua.setAvaBalance(old.subtract(o.getPayAmount()));
		userAssestService.updateAssest(ua);

		// 购买成功 修改订单状态
		o.setPayStatus(Status.YES.ordinal());
		o.setPaymentTime(Utility.currentTimestamp());
		o.setPayStatus(Status.YES.ordinal());
		o.setOrderStatus(OrderStatus.PENDING_SEND.ordinal());
		update(o);

		o.setCoinString(coin.getName());
		Product p = productService.findProductBySn(o.getProductSn());
		SunRecord record = new SunRecord();
		record.setOld(old);
		record.setSend(o.getUserId());
		record.setReceice(p.getSellerId());
		record.setNow(ua.getAvaBalance());
		record.setAmount(o.getPayAmount());
		record.setDirection(Type.PAY_ORDER.getDirection());
		record.setType(Type.PAY_ORDER.getId());
		record.setCreateTime(Utility.currentTimestamp());
		spuRecordService.insert(record);

		// 修改销量
		p.setSale(p.getSale() + o.getNumber());
		productService.update(p);
		// 用户升级 黄金创客 合伙人 线程
		new OrderThread(userService, this, o.getUserId(), configService).start();

		return o;
	}

	@Override
	public Order findOrderByCondition(Order order) throws WebAppException {
		return orderMapper.selectOne(order);
	}

	@Override
	public BigDecimal sumOrdersAmountByUserId(Long userId) throws WebAppException {
		return orderMapper.sumOrdersAmountByUserId(userId);
	}

	@Override
	@Transactional
	public void sureReceive(String orderSn, Long userId, Long pid) throws WebAppException {
		Order order = new Order();
		order.setOrderSn(orderSn);
		order.setUserId(userId);
		order.setPayStatus(Status.YES.ordinal());
		order.setIsDelete(Status.NO.ordinal());
		Order o = orderMapper.selectOne(order);
		if (Utility.isNull(o)) {
			throw new WebAppException(ExceptionConstants.ORDER_NOT_FOUND);
		}
		o.setOrderStatus(OrderStatus.FINISH.ordinal());
		o.setCompleteTime(Utility.currentTimestamp());
		o.setUpdateTime(Utility.currentTimestamp());
		o.setSigningTime(Utility.currentTimestamp());
		o.setIsDelete(Status.NO.ordinal());
		orderMapper.updateByPrimaryKeySelective(o);

		sendFreeMca(o);
		o.setPid(pid);
		// 直推收益
		directPushProfit(o);
		// 团队收益
		teamProfit(o);
		// 平级收益
		levelProfit(o);
		// 放入分红池
		putPool(o);
	}

	public void sendFreeMca(Order o) {
		Coin coin = coinService.getCoinByName(Constants.MCA);
		UserAssest ua = userAssestService.findAssestByUserIdAndCoinId(o.getUserId(), coin.getId());
		BigDecimal amount = BigDecimal.ZERO;
		if (o.getPayAmount().compareTo(new BigDecimal(360)) >= 0
				&& o.getPayAmount().compareTo(new BigDecimal(20000)) <= 0) {
			amount = amount.add(ua.getAvaBalance()).add(new BigDecimal(360));
			McaRecord record = new McaRecord(null, null, Type.PAY_ORDER_SEND.getId(), ua.getAvaBalance(), amount,
					new BigDecimal(360), o.getUserId(), Utility.currentTimestamp(), Type.PAY_ORDER_SEND.getDirection());
			mcaRecordService.insert(record);
		} else if (o.getPayAmount().compareTo(new BigDecimal(20000)) >= 0) {
			amount = amount.add(ua.getAvaBalance()).add(new BigDecimal(60000));
			McaRecord record = new McaRecord(null, null, Type.PAY_ORDER_SEND.getId(), ua.getAvaBalance(), amount,
					new BigDecimal(60000), o.getUserId(), Utility.currentTimestamp(),
					Type.PAY_ORDER_SEND.getDirection());
			mcaRecordService.insert(record);
		}
		ua.setAvaBalance(amount);
		userAssestService.updateAssest(ua);
	}

	/**
	 * 分配直推收益
	 * 
	 * @param o
	 */
	@Transactional
	public void directPushProfit(Order o) {
		User parent = userService.getUserById(o.getPid());
		// 推荐人必须是黄金创客或者合伙人
		if (Utility.isNotNull(parent) && parent.getLevel() > Level.General.ordinal()) {
			int level = parent.getLevel();
			long userId = parent.getId();
			Config config = null;
			Config coinConfig = null;
			if (level == Level.GoldUser.ordinal()) {
				config = configService.findConfigByKey(ConfigKeys.GOLD_DIRECT_PUSH_PROFIT.getKey());
				coinConfig = configService.findConfigByKey(ConfigKeys.GOLD_DIRECT_PUSH_PROFIT_TYPE.getKey());
			} else if (level == Level.Partnet.ordinal()) {
				config = configService.findConfigByKey(ConfigKeys.PARTNET_DIRECT_PUSH_PROFIT.getKey());
				coinConfig = configService.findConfigByKey(ConfigKeys.PARTNET_DIRECT_PUSH_PROFIT_TYPE.getKey());
			}

			Coin mcacoin = coinService.getCoinByName(Constants.MCA);
			UserAssest mcaAssest = userAssestService.findAssestByUserIdAndCoinId(userId, mcacoin.getId());
			Coin rmbcoin = coinService.getCoinByName(Constants.RMB);
			UserAssest rmbAssest = userAssestService.findAssestByUserIdAndCoinId(userId, rmbcoin.getId());
			if (Utility.isNotNull(mcaAssest) && Utility.isNotNull(mcacoin) && Utility.isNotNull(rmbAssest)
					&& Utility.isNotNull(rmbcoin)) {
				BigDecimal profit = o.getPayAmount().multiply(new BigDecimal(config.getValue()));
				BigDecimal pushProfit = o.getPayAmount().multiply(new BigDecimal(config.getValue()))
						.multiply(new BigDecimal(coinConfig.getValue()));

				BigDecimal now = mcaAssest.getAvaBalance().add(pushProfit);
				McaRecord mca = new McaRecord(null, o.getUserId(), Type.BACK_PROFIT.getId(), mcaAssest.getAvaBalance(),
						now, pushProfit, userId, Utility.currentTimestamp(), Type.BACK_PROFIT.getDirection());

				BigDecimal rmbnow = rmbAssest.getAvaBalance().add(profit.subtract(pushProfit));
				RmbRecord rmb = new RmbRecord(null, o.getUserId(), Type.BACK_PROFIT.getId(), rmbAssest.getAvaBalance(),
						rmbnow, profit.subtract(pushProfit), userId, Utility.currentTimestamp(),
						Type.BACK_PROFIT.getDirection());

				// 更新人民币资产
				userAssestService.updateAssest(new UserAssest(rmbAssest.getId(), userId, rmbcoin.getId(), rmbnow));
				// 记录人民币收益流水
				rmbRecordService.insert(rmb);
				// 更新MCA资产
				userAssestService.updateAssest(new UserAssest(mcaAssest.getId(), userId, mcacoin.getId(), now));
				// 记录MCA收益流水
				mcaRecordService.insert(mca);
			}
		}
	}

	/**
	 * 分配团队收益
	 * 
	 * @param o
	 */
	@Transactional
	public void teamProfit(Order o) {
		User u = userService.getParentSuperNodeByUserId(o.getUserId(), null);
		// 有团队长的存在 则给收益,没有就不给
		if (Utility.isNotNull(u) && u.getId() != o.getUserId()) {
			Config config = configService.findConfigByKey(ConfigKeys.TEAM_PROFIT.getKey());
			if (Utility.isNotNull(config)) {
				BigDecimal profit = o.getPayAmount().multiply(new BigDecimal(config.getValue()));
				Coin coin = coinService.getCoinByName(Constants.MCA);
				UserAssest ua = userAssestService.findAssestByUserIdAndCoinId(u.getId(), coin.getId());
				if (Utility.isNotNull(ua) && Utility.isNotNull(coin)) {

					McaRecord record = new McaRecord();
					record.setAmount(profit);
					record.setReceice(u.getId());
					record.setSend(o.getUserId());
					record.setDirection(Type.TEAM_PROFIT.getDirection());
					record.setType(Type.TEAM_PROFIT.getId());
					record.setCreateTime(Utility.currentTimestamp());
					record.setOld(ua.getAvaBalance());
					BigDecimal now = ua.getAvaBalance().add(profit);
					record.setNow(now);

					// 更新资产
					userAssestService.updateAssest(new UserAssest(ua.getId(), u.getId(), coin.getId(), now));

					// 记录收益流水
					mcaRecordService.insert(record);
				}

			}
		}
	}

	/**
	 * 平级收益
	 * 
	 * @param o
	 */
	@Transactional
	public void levelProfit(Order o) {
		// 先找他的团队长
		User u = userService.getParentSuperNodeByUserId(o.getUserId(), null);
		if (Utility.isNotNull(u) && u.getId() != o.getUserId()) {
			// 再找团队长的团队长
			User levelUser = userService.getLevelSuperNodeByUserId(u.getId());
			// 有团队长的存在 则给收益,没有就不给
			if (Utility.isNotNull(levelUser) && levelUser.getId() != u.getId()) {
				Config config = configService.findConfigByKey(ConfigKeys.LEVEL_PROFIT.getKey());
				if (Utility.isNotNull(config)) {
					BigDecimal profit = o.getPayAmount().multiply(new BigDecimal(config.getValue()));
					Coin coin = coinService.getCoinByName(Constants.MCA);
					UserAssest ua = userAssestService.findAssestByUserIdAndCoinId(levelUser.getId(), coin.getId());
					if (Utility.isNotNull(ua) && Utility.isNotNull(coin)) {

						McaRecord record = new McaRecord();
						record.setAmount(profit);
						record.setReceice(levelUser.getId());
						record.setSend(o.getUserId());
						record.setDirection(Type.LEVEL_PROFIT.getDirection());
						record.setType(Type.LEVEL_PROFIT.getId());
						record.setCreateTime(Utility.currentTimestamp());
						record.setOld(ua.getAvaBalance());
						BigDecimal now = ua.getAvaBalance().add(profit);
						record.setNow(now);

						// 更新资产
						userAssestService
								.updateAssest(new UserAssest(ua.getId(), levelUser.getId(), coin.getId(), now));

						// 记录收益流水
						mcaRecordService.insert(record);
					}

				}
			}
		}
	}

	/**
	 * 放入分红池
	 * 
	 * @param o
	 */
	@Transactional
	public void putPool(Order o) {
		Config config = configService.findConfigByKey(ConfigKeys.EVERY_SYSTEM_BONUS_POOL.getKey());
		if (Utility.isNotNull(config) && o.getPayAmount().compareTo(new BigDecimal(config.getValue())) >= 0) {
			Coin coin = coinService.getCoinByName(Constants.MCA);
			if (Utility.isNotNull(coin)) {
				BonusPool bp = bonusPoolService.getBonusPoolByCoinId(coin.getId());
				if (Utility.isNull(bp)) {
					// 初始化分红池
					bp = bonusPoolService.init(coin.getId());
				}
				BigDecimal amount = new BigDecimal(config.getValue());
				bp.setMayNumber(bp.getMayNumber().add(amount));

				BonusPoolRecord bpr = new BonusPoolRecord();
				bpr.setCoinId(coin.getId());
				bpr.setAmount(amount);
				bpr.setCreateTime(Utility.currentTimestamp());
				bpr.setUserId(o.getUserId());
				bpr.setDirection(Direction.PUSH.ordinal());

				// 记录分红数据
				bonusPoolRecordService.insert(bpr);

				// 更新分红池金额
				bonusPoolService.update(bp);
			}
		}
	}

	@Override
	public void cancel(Order order) throws WebAppException {
		Order o = findOrderByCondition(new Order(order.getOrderSn()));
		if (Utility.isNull(o)) {
			throw new WebAppException(ExceptionConstants.ORDER_NOT_FOUND);
		}
		o.setOrderStatus(order.getOrderStatus());
		o.setCancelReason(order.getCancelReason());
		orderMapper.updateByPrimaryKeySelective(o);
	}

	@Override
	public void delete(String orderSn) throws WebAppException {
		Order o = findOrderByCondition(new Order(orderSn));
		if (Utility.isNull(o)) {
			throw new WebAppException(ExceptionConstants.ORDER_NOT_FOUND);
		}
		o.setIsDelete(Status.YES.ordinal());
		orderMapper.updateByPrimaryKeySelective(o);
	}

	@Override
	public Order detail(String orderSn) throws WebAppException {
		Order o = findOrderByCondition(new Order(orderSn));
		if (Utility.isNull(o)) {
			throw new WebAppException(ExceptionConstants.ORDER_NOT_FOUND);
		}
		Product p = productService.findProductBySn(o.getProductSn());
		if (Utility.isNotNull(p)) {

			Coin coin = coinService.getCoinById(p.getCoinId());
			if (Utility.isNotNull(coin)) {
				o.setCoinString(coin.getName());
			}

			o.setPic(p.getImgUrl());
			o.setProductName(p.getProductName());
			o.setProductDesc(p.getDescription());
			o.setPayAmount(o.getPayAmount().setScale(2));
			o.setOrderAmount(o.getOrderAmount().setScale(2));
			if (p.getPayType() != PayType.SUN.ordinal()) {
				BigDecimal orderMoney = o.getAmount().multiply(new BigDecimal(o.getNumber()))
						.add(Utility.isZero(new BigDecimal(p.getFreight())));
				o.setOrderAmount(orderMoney);
				o.setFreight(p.getFreight());
				o.setPayAmount(orderMoney);
			}
		}

		return o;
	}

	@Override
	@Transactional
	public void success(String orderSn) throws WebAppException {
		Order order = new Order();
		order.setOrderSn(orderSn);
		order.setIsDelete(Status.NO.ordinal());
		Order o = orderMapper.selectOne(order);
		if (Utility.isNull(o)) {
			throw new WebAppException(ExceptionConstants.ORDER_NOT_FOUND);
		}
		o.setPayStatus(Status.YES.ordinal());
		o.setOrderStatus(OrderStatus.PENDING_SEND.ordinal());
		o.setUpdateTime(Utility.currentTimestamp());
		o.setPaymentTime(Utility.currentTimestamp());
		orderMapper.updateByPrimaryKeySelective(o);

		Product p = productService.findProductBySn(o.getProductSn());
		if (Utility.isNotNull(p)) {
			// 修改销量
			p.setSale(p.getSale() + o.getNumber());
			productService.update(p);
		}

		// 用户升级 黄金创客 合伙人 线程
		new OrderThread(userService, this, o.getUserId(), configService).start();
	}

	@Override
	public void fail(String orderSn) throws WebAppException {
		// TODO Auto-generated method stub

	}

	@Override
	public Order getUnifiedOrder(String orderSn) throws WebAppException {
		Order o = new Order();
		o.setOrderSn(orderSn);
		o.setIsDelete(Status.NO.ordinal());
		o.setPayStatus(Status.NO.ordinal());
		return findOrderByCondition(o);
	}

	@Override
	public Map<String, Object> wechatPay(HttpServletRequest request, String orderSn)
			throws UnsupportedEncodingException, Exception, WebAppException {
		String prepayId = createUnifiedorder(request, getUnifiedOrder(orderSn));
		if (Utility.isNull(prepayId)) {
			throw new WebAppException(ExceptionConstants.CREATE_UNIFIED_ORDER);
		}
		// 获得参数(微信统一下单接口生成的prepay_id )
		// 创建 随机串
		String timeStamp = String.valueOf(Utility.currentTimestamp());
		String nonceStr = (UUID.randomUUID()).toString().replaceAll("-", "");
		SortedMap<String, String> paraMap = new TreeMap<String, String>();
		paraMap.put("appid", APPID);
		paraMap.put("partnerid", MCHID);
		paraMap.put("prepayid", prepayId);
		paraMap.put("timestamp", timeStamp);
		paraMap.put("noncestr", nonceStr);
		paraMap.put("package", "Sign=WXPay");
		// 设置(签名方式)
		// 第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。(签名)
		String sign = createSign("utf-8", paraMap);
		Map<String, Object> mapData = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(sign)) {
			// 返回签名信息
			mapData.put("nonce_str", nonceStr);
			mapData.put("paySign", sign);
			mapData.put("appId", APPID);
			mapData.put("mch_id", MCHID);
			mapData.put("partnerid", MCHID);
			mapData.put("prepay_id", prepayId);
			// 返回时间戳
			mapData.put("timeStamp", timeStamp);
			// 返回数据包
			mapData.put("msg", "签名校验成功");
			return mapData;
		}
		return null;
	}

	/**
	 * 创建预付单号
	 * 
	 * @param request
	 * @param order
	 * @return
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public String createUnifiedorder(HttpServletRequest request, Order order)
			throws UnsupportedEncodingException, Exception {
		SortedMap<String, String> paraMap = new TreeMap<String, String>();
		Product p = productService.findProductBySn(order.getProductSn());
		if (Utility.isNotNull(p)) {
			order.setProductName(p.getProductName());
		}
		String body = "购买商品: " + order.getProductName();
		String nonceStr = (UUID.randomUUID()).toString().replaceAll("-", "");
		paraMap.put("appid", APPID);
		paraMap.put("mch_id", MCHID);
		paraMap.put("nonce_str", nonceStr);
		paraMap.put("body", body);
		paraMap.put("out_trade_no", order.getOrderSn());
		// 设置请求参数(总金额)
		paraMap.put("total_fee", order.getPayAmount().multiply(new BigDecimal(100)).intValue() + "");
		paraMap.put("spbill_create_ip", Utility.getRemoteAddr(request));
		// 设置请求参数(通知地址)
		paraMap.put("notify_url", PAYCALLBACKURL);
		// 设置请求参数(交易类型)
		paraMap.put("trade_type", "APP");
		// 设置请求参数(openid)(在接口文档中 该参数 是否必填项 但是一定要注意 如果交易类型设置成'JSAPI'则必须传入openid)
		// 调用逻辑传入参数按照字段名的 ASCII 码从小到大排序（字典序）
		String stringA = Utility.formatUrlMap(paraMap, false, false);
		// 第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。(签名)
		String sign = MD5.getMD532((stringA + "&key=" + APIKEY)).toUpperCase();

		StringBuffer paramBuffer = new StringBuffer();
		paramBuffer.append("<xml>");
		paramBuffer.append("<appid>" + APPID + "</appid>");
		paramBuffer.append("<mch_id>" + MCHID + "</mch_id>");
		paramBuffer.append("<nonce_str>" + paraMap.get("nonce_str") + "</nonce_str>");
		paramBuffer.append("<sign>" + sign + "</sign>");
		paramBuffer.append("<body>" + body + "</body>");
		paramBuffer.append("<out_trade_no>" + paraMap.get("out_trade_no") + "</out_trade_no>");
		paramBuffer.append("<total_fee>" + paraMap.get("total_fee") + "</total_fee>");
		paramBuffer.append("<spbill_create_ip>" + paraMap.get("spbill_create_ip") + "</spbill_create_ip>");
		paramBuffer.append("<notify_url>" + paraMap.get("notify_url") + "</notify_url>");
		paramBuffer.append("<trade_type>" + paraMap.get("trade_type") + "</trade_type>");
		paramBuffer.append("</xml>");
		log.info("微信支付传递参数:" + paramBuffer.toString());
		// 发送请求(POST)(获得数据包ID)(这有个注意的地方 如果不转码成ISO8859-1则会告诉你body不是UTF8编码
		// 就算你改成UTF8编码也一样不好使 所以修改成ISO8859-1)
		Map<String, String> map = Utility.doXMLParse(Utility.getRemotePortData(UNIFIEDORDERURL,
				new String(paramBuffer.toString().getBytes("UTF-8"), "ISO8859-1")));
		log.info("返回支付预订单号是:" + map.toString());
		if (map != null) {
			if (("SUCCESS").equals(map.get("result_code"))) {
				return map.get("prepay_id");
			}
		}
		return null;
	}

	public String createSign(String characterEncoding, SortedMap<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + APIKEY);
		String sign = MD5.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}

}
