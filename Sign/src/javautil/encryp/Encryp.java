package javautil.encryp;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
public class Encryp {
	/**
	 * 문자열이 숫자인지 판단하는 함수
	 */
	public static boolean isNumber(String str) {
		boolean result = false;

		try {
			Double.parseDouble(str);
			result = true;
		} catch (Exception e) {
		}

		return result;
	}

	/**
	 * 문자열을 MD-5 방식으로 암호화
	 *
	 * @param txt
	 *            암호화 하려하는 문자열
	 * @return String
	 * @throws Exception
	 */
	public static String getEncMD5(String txt) throws Exception {

		StringBuffer sbuf = new StringBuffer();

		MessageDigest mDigest = MessageDigest.getInstance("MD5");
		mDigest.update(txt.getBytes());

		byte[] msgStr = mDigest.digest();

		for (int i = 0; i < msgStr.length; i++) {
			String tmpEncTxt = Integer.toHexString((int) msgStr[i] & 0x00ff);
			sbuf.append(tmpEncTxt);
		}
		return sbuf.toString();
	}

	/**
	 * 문자열을 SHA-256 방식으로 암호화
	 *
	 * @param txt
	 *            암호화 하려하는 문자열
	 * @return String
	 * @throws Exception
	 */
	public static String getEncSHA256(String txt) throws Exception {
		StringBuffer sbuf = new StringBuffer();

		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
		mDigest.update(txt.getBytes());

		byte[] msgStr = mDigest.digest();

		for (int i = 0; i < msgStr.length; i++) {
			byte tmpStrByte = msgStr[i];
			String tmpEncTxt = Integer.toString((tmpStrByte & 0xff) + 0x100, 16).substring(1);

			sbuf.append(tmpEncTxt);
		}
		return sbuf.toString();
	}
}
