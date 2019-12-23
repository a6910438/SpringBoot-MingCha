package com.xhr.mca.common;

import java.security.MessageDigest;

/**
 * 描述：
 * <p>
 * 加密字符串
 * </p>
 * 
 * @author xiejiong
 */
public final class MD5 {

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	private MD5() {
	}

	/**
	 * 获取input对应的MD5代码
	 * 
	 * @param input
	 * @return
	 */
	public static final String getMD5(String input) {
		try {
			byte[] inputByte = input.getBytes("UTF-8");
			MessageDigest md;
			md = MessageDigest.getInstance("md5");

			md.update(inputByte);
			byte[] digest = md.digest();
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < digest.length; i++) {
				int val = ((int) digest[i]) & 0xff;
				if (val < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(val));
			}
			return buf.toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static final String getMD516(String input) {
		String md5 = getMD5(input);
		return md5.substring(8, 24);
	}

	public static final String getMD532(String input) {
		return getMD5(input);
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
}
