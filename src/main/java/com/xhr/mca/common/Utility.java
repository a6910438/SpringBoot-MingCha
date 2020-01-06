package com.xhr.mca.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;

/**
 * 基础工具类
 * 
 * @author Huang Sheng
 *
 */
public class Utility {

	/**
	 * 判断对象是否为null , 为null返回true,否则返回false
	 * 
	 * @param obj 被判断的对象
	 * @return boolean
	 */
	public static boolean isNull(Object obj) {
		return (null == obj) ? true : false;
	}

	/**
	 * 判断对象是否为null , 为null返回false,否则返回true
	 *
	 * @param obj 被判断的对象
	 * @return boolean
	 */
	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

	/**
	 * 判断字符串是否为null或者0长度，字符串在判断长度时，先去除前后的空格,空或者0长度返回false,否则返回true
	 *
	 * @param str 被判断的字符串
	 * 
	 * @return boolean
	 */
	public static boolean isNotNullOrZeroLenght(String str) {
		return !isNullOrZeroLenght(str);
	}

	/**
	 * 判断str数组是否为null或者0长度，只要有一个空或者0长度返回false, 否则返回true
	 *
	 * @param str String 字符数组
	 * @return boolean
	 * @author huanghui
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isNotNullOrZeroLenght(String... str) {
		for (String s : str) {
			if (isNullOrZeroLenght(s)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断str数组是否为null或者0长度，只要有一个空或者0长度返回true, 否则返回false
	 *
	 * @param str String 字符数组
	 * @return boolean
	 * @author huanghui
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isNullOrZeroLenght(String... str) {
		return !isNotNullOrZeroLenght(str);
	}

	/**
	 * 判断集合对象是否为null或者0大小 , 为空或0大小返回true ,否则返回false
	 *
	 * @param c collection 集合接口
	 * @return boolean 布尔值
	 * 
	 * @author huanghui
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isNullOrZeroSize(Collection<? extends Object> c) {
		return isNull(c) || c.isEmpty();
	}

	/**
	 * 判断集合对象是否为null或者0大小 , 为空或0大小返回false, 否则返回true
	 *
	 * @param c collection 集合接口
	 * @return boolean 布尔值
	 * 
	 * @author huanghui
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isNotNullOrZeroSize(Collection<? extends Object> c) {
		return !isNullOrZeroSize(c);
	}

	/**
	 * 判断数字类型是否为null或者0，如果是返回true，否则返回false
	 *
	 * @param number 被判断的数字
	 * @return boolean
	 */
	public static boolean isNullOrZero(Number number) {
		if (Utility.isNotNull(number)) {
			return (number.doubleValue() != 0) ? false : true;
		}
		return true;
	}

	/**
	 * 判断数字类型是否不为null或者0，如果是返回true，否则返回false
	 *
	 * @param number 被判断的数字
	 * @return boolean
	 */
	public static boolean isNotNullOrZero(Number number) {
		return !isNullOrZero(number);
	}

	/**
	 * 将java.util.Date类型转化位String类型
	 *
	 * @param date   要转换的时间
	 * @param format 时间格式
	 * @return 如果转换成功，返回指定格式字符串，如果转换失败，返回null
	 */
	public static String date2String(Date date, String format) {
		if (Utility.isNull(date) || Utility.isNull(format)) {
			return null;
		}

		return DateFormatUtils.format(date, format);
	}

	/**
	 * 将字符串时间转换成java.util.Date类型
	 * 
	 * @param str    要转换的字符串
	 * @param format 时间格式
	 * @return 如果转换失败，返回null
	 */
	public static Date string2Date(String str, String format) {
		if (Utility.isNull(str) || Utility.isNull(format)) {
			return null;
		}

		// 定义日期/时间格式
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date;

		try {
			// 转换日期/时间格式
			date = sdf.parse(str);
			// 判断转换前后时间是否一致

			if (!str.equals(sdf.format(date))) {
				date = null;
			}
		} catch (ParseException e) {
			date = null;
		}
		return date;
	}

	/**
	 * BigDecimal的加法运算封装
	 * 
	 * @param b1
	 * @param bn
	 * @return
	 */
	public static BigDecimal safeAdd(BigDecimal b1, BigDecimal... bn) {
		if (null == b1) {
			b1 = BigDecimal.ZERO;
		}
		if (null != bn) {
			for (BigDecimal b : bn) {
				b1 = b1.add(null == b ? BigDecimal.ZERO : b);
			}
		}
		return b1;
	}

	/**
	 * Integer加法运算的封装
	 * 
	 * @param b1 第一个数
	 * @param bn 需要加的加法数组
	 * @注 ： Optional 是属于com.google.common.base.Optional<T> 下面的class
	 * @return
	 */
	public static Integer safeAdd(Integer b1, Integer... bn) {
		if (null == b1) {
			b1 = 0;
		}
		Integer r = b1;
		if (null != bn) {
			for (Integer b : bn) {
				r += Optional.fromNullable(b).or(0);
			}
		}
		return r > 0 ? r : 0;
	}

	/**
	 * 计算金额方法
	 * 
	 * @param b1
	 * @param bn
	 * @return
	 */
	public static BigDecimal safeSubtract(BigDecimal b1, BigDecimal... bn) {
		return safeSubtract(true, b1, bn);
	}

	/**
	 * BigDecimal的安全减法运算
	 * 
	 * @param isZero 减法结果为负数时是否返回0，true是返回0（金额计算时使用），false是返回负数结果
	 * @param b1     被减数
	 * @param bn     需要减的减数数组
	 * @return
	 */
	public static BigDecimal safeSubtract(Boolean isZero, BigDecimal b1, BigDecimal... bn) {
		if (null == b1) {
			b1 = BigDecimal.ZERO;
		}
		BigDecimal r = b1;
		if (null != bn) {
			for (BigDecimal b : bn) {
				r = r.subtract((null == b ? BigDecimal.ZERO : b));
			}
		}
		return isZero ? (r.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : r) : r;
	}

	/**
	 * 整型的减法运算，小于0时返回0
	 * 
	 * @param b1
	 * @param bn
	 * @return
	 */
	public static Integer safeSubtract(Integer b1, Integer... bn) {
		if (null == b1) {
			b1 = 0;
		}
		Integer r = b1;
		if (null != bn) {
			for (Integer b : bn) {
				r -= Optional.fromNullable(b).or(0);
			}
		}
		return null != r && r > 0 ? r : 0;
	}

	/**
	 * 金额除法计算，返回2位小数（具体的返回多少位大家自己看着改吧）
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static <T extends Number> BigDecimal safeDivide(T b1, T b2) {
		return safeDivide(b1, b2, BigDecimal.ZERO);
	}

	/**
	 * BigDecimal的除法运算封装，如果除数或者被除数为0，返回默认值 默认返回小数位后2位，用于金额计算
	 * 
	 * @param b1
	 * @param b2
	 * @param defaultValue
	 * @return
	 */
	public static <T extends Number> BigDecimal safeDivide(T b1, T b2, BigDecimal defaultValue) {
		if (null == b1 || null == b2) {
			return defaultValue;
		}
		try {
			return BigDecimal.valueOf(b1.doubleValue()).divide(BigDecimal.valueOf(b2.doubleValue()), 12,
					BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * BigDecimal的乘法运算封装
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static <T extends Number> BigDecimal safeMultiply(T b1, T b2) {
		if (null == b1 || null == b2) {
			return BigDecimal.ZERO;
		}
		return BigDecimal.valueOf(b1.doubleValue()).multiply(BigDecimal.valueOf(b2.doubleValue())).setScale(2,
				BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal conventBigDecimal(Double d) {
		BigDecimal b = null;
		if (Utility.isNotNull(d)) {
			b = new BigDecimal(d);
		}
		return b;
	}

	/**
	 * 计算两个时间相差的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getGapCount(Date startDate, Date endDate) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}

	public static String getNowDate() {
		String temp_str = "";
		Date dt = new Date();
		// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
		temp_str = sdf.format(dt);
		return temp_str;
	}

	public static String getCurrentYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date date = new Date();
		return sdf.format(date);
	}

	public static String getCurrentMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Date date = new Date();
		return sdf.format(date);
	}

	public static String getCurrentDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date date = new Date();
		return sdf.format(date);
	}

	public static String getCurrentHour() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		Date date = new Date();
		return sdf.format(date);
	}

	public static String getCurrentMinute() {
		SimpleDateFormat sdf = new SimpleDateFormat("mm");
		Date date = new Date();
		return sdf.format(date);
	}

	public static String getCurrentSecond() {
		SimpleDateFormat sdf = new SimpleDateFormat("ss");
		Date date = new Date();
		return sdf.format(date);
	}

	// 生成随机数字和字母,
	public static String getStringRandom(int length) {
		String val = "";
		Random random = new Random();
		// length为几位密码
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	public static String createWaitOrderId(String stockName, Integer type) {
		String time = getLondonTime();
		String way = type == 0 ? "L" : "S";
		return stockName + "-" + way + "-" + time + "-" + getStringRandom(6);
	}

	public static String createOrderId(String stockName, Integer type) {
		String time = getLondonTime();
		String way = type == 0 ? "L" : "S";
		return stockName + "-" + way + "-" + time + "-" + getStringRandom(4);
	}

	/**
	 * 获取伦敦时间
	 * 
	 * @return
	 */
	public static String getLondonTime() {
		TimeZone timeZone = TimeZone.getTimeZone("GMT+1:00");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		simpleDateFormat.setTimeZone(timeZone);
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 获取外网IP，没有就返回内网ip
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static String getRealIp() throws SocketException {
		String localip = null;// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP
		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		boolean finded = false;// 是否找到外网IP
		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
					netip = ip.getHostAddress();
					finded = true;
					break;
				} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
					localip = ip.getHostAddress();
				}
			}
		}
		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}

	/**
	 * 创建日期:2018年4月6日<br/>
	 * 代码创建:黄聪<br/>
	 * 功能描述:通过request来获取到json数据<br/>
	 * 
	 * @param         <T>
	 * 
	 * @param request
	 * @return
	 * @return
	 */
	public static <T> T getJSONParam(HttpServletRequest request, Class<T> t) {
		JSONObject jsonParam = null;
		T q = null;
		try {
			// 获取输入流
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

			// 写入数据到Stringbuilder
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = streamReader.readLine()) != null) {
				sb.append(line);
			}
			jsonParam = JSONObject.parseObject(sb.toString());
			q = JSONObject.toJavaObject(jsonParam, t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return q;
	}

	public static int getRandomNumber(int length) {
		int code = 0;
		if (length == 4) {
			code = (int) (Math.random() * 9000 + 1000);
		} else if (length == 5) {
			code = (int) (Math.random() * 90000 + 10000);
		} else if (length == 6) {
			code = (int) (Math.random() * 900000 + 100000);
		}
		return code;
	}

	public static String getRandomString(int length) {
		int code = 0;
		if (length == 4) {
			code = (int) (Math.random() * 9000 + 1000);
		} else if (length == 5) {
			code = (int) (Math.random() * 90000 + 10000);
		} else if (length == 6) {
			code = (int) (Math.random() * 900000 + 100000);
		}
		return String.valueOf(code);
	}

	public static long currentTimestamp() {
		return System.currentTimeMillis() / 1000;
	}

	public static BigDecimal isZero(BigDecimal number) {
		if (number == null) {
			return BigDecimal.ZERO;
		}
		if (number.compareTo(new BigDecimal(0E-16)) == 0 || number.compareTo(new BigDecimal(0E-8)) == 0
				|| number.compareTo(new BigDecimal(5.0E-4)) == 0) {
			return BigDecimal.ZERO;
		}
		return number;
	}

	/**
	 * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
	 * 
	 */
	public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				@Override
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());

				}

			});
			// 构造URL 键值对的格式
			StringBuilder buf = new StringBuilder();
			for (Map.Entry<String, String> item : infoIds) {
				if (StringUtils.isNotBlank(item.getKey())) {
					String key = item.getKey();
					String val = item.getValue();
					if (urlEncode) {
						val = URLEncoder.encode(val, "utf-8");

					}
					if (keyToLower) {
						buf.append(key.toLowerCase() + "=" + val);

					} else {
						buf.append(key + "=" + val);

					}
					buf.append("&");

				}

			}
			buff = buf.toString();
			if (buff.isEmpty() == false) {
				buff = buff.substring(0, buff.length() - 1);

			}

		} catch (Exception e) {
			return null;

		}
		return buff;
	}

	/**
	 * 方法名: getRemotePortData 描述: 发送远程请求 获得代码示例 参数： @param urls 访问路径
	 * 
	 * 
	 */
	public static String getRemotePortData(String urls, String param) {

		try {
			URL url = new URL(urls);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置连接超时时间
			conn.setConnectTimeout(30000);
			// 设置读取超时时间
			conn.setReadTimeout(30000);
			conn.setRequestMethod("POST");
			if (!StringUtils.isBlank(param)) {
				conn.setRequestProperty("Origin", "https://sirius.searates.com");// 主要参数
				conn.setRequestProperty("Referer",
						"https://sirius.searates.com/cn/port?A=ChIJP1j2OhRahjURNsllbOuKc3Y&D=567&G=16959&shipment=1&container=20st&weight=1&product=0&request=&weightcargo=1&");
				conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");// 主要参数
			}
			// 需要输出
			conn.setDoInput(true);
			// 需要输入
			conn.setDoOutput(true);
			// 设置是否使用缓存
			conn.setUseCaches(false);
			// 设置请求属性
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			conn.setRequestProperty("Charset", "UTF-8");

			if (!StringUtils.isBlank(param)) {
				// 建立输入流，向指向的URL传入参数
				DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(param);
				dos.flush();
				dos.close();
			}
			// 输出返回结果
			InputStream input = conn.getInputStream();
			int resLen = 0;
			byte[] res = new byte[1024];
			StringBuilder sb = new StringBuilder();
			while ((resLen = input.read(res)) != -1) {
				sb.append(new String(res, 0, resLen));
			}
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Map<String, String> doXMLParse(String strxml) throws Exception {
		if (null == strxml || "".equals(strxml)) {
			return null;
		}
		Map<String, String> m = new HashMap<String, String>();
		InputStream in = String2Inputstream(strxml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}

			m.put(k, v);
		}
		// 关闭流
		in.close();
		return m;
	}

	@SuppressWarnings("rawtypes")
	private static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}

	private static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}

	/**
	 * 获取用户ip
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}

		// 从head的什么值获取IP地址
		if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("X-Real-IP");
		}

		if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
			ip = request.getRemoteAddr();
		}
		if (StringUtils.isNotBlank(ip) && StringUtils.indexOf(ip, ",") > 0) {
			String[] ipArray = StringUtils.split(ip, ",");
			ip = ipArray[0];
		}
		return ip;
	}

	public static int getRandomInt(int min, int max) {
		Random random = new Random();
		return random.nextInt(max) % (max - min + 1) + min;
	}
}
