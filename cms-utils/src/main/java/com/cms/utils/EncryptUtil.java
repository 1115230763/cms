package com.cms.utils;

import it.sauronsoftware.base64.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具類
 */
public class EncryptUtil {

	public static void main(String[] args) {
		System.out.println(EncryptUtil.md5AndSha("123"));
	}

	/**
	 * 使用Base64加密
	 * @param inputText
	 * @param charset
	 * @return
	 */
	public static String base64Encode(String inputText, String... charset) {
		if (charset.length == 1) {
			return Base64.encode(inputText, charset[0]);
		} else {
			return Base64.encode(inputText);
		}
	}

	/**
	 * Base64解密
	 * @param inputText
	 * @param charset
	 * @return
	 */
	public static String base64Decode(String inputText, String... charset) {
		if (charset.length == 1) {
			return Base64.decode(inputText, charset[0]);
		} else {
			return Base64.decode(inputText);
		}
	}

	/**
	 * 二次加密 md5后 sha
	 * @param inputText
	 * @return
	 */
	public static String md5AndSha(String inputText) {
		return sha(md5(inputText));
	}

	/**
	 * md5加密 长度为32位
	 * @param inputText
	 * @return
	 */
	public static String md5(String inputText) {
		return encrypt(inputText, "md5");
	}

	/**
	 * sha加密 长度为40位
	 * @param inputText
	 * @return
	 */
	public static String sha(String inputText) {
		return encrypt(inputText, "sha-1");
	}

	/**
	 * md5或者sha-1加密
	 * @param inputText 要加密的内容
	 * @param algorithmName 加密算法名称：md5或者sha-1，不分大小写
	 * @return
	 */
	private static String encrypt(String inputText, String algorithmName) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入加密內容");
		}
		if (algorithmName == null || "".equals(algorithmName.trim())) {
			algorithmName = "md5";
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF8"));
			byte s[] = m.digest();
			// m.digest(inputText.getBytes("UTF8"));
			return hex(s);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}

	/**
	 * 返回十六进制字符串
	 * @param arr
	 * @return
	 */
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

}
