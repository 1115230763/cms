package com.cms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;



public class SendSMSUtil {
	
	private static Logger log = Logger.getLogger(SendSMSUtil.class);
	
	/**
	 * 短信网关地址
	 */
	public static final String URL = "http://api5.nashikuai.cn/SendSms.aspx";

	public static final String BALANCE_URL = "http://api5.nashikuai.cn/GetBalance.aspx";

	/**
	 * 短信网关用户名
	 */
	public static final String USERNAME = "wyxx";

	/**
	 * 密码
	 */
	public static final String PASSWORD = "wyxx0616";

	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url 发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendMesPost(String mobile, String msgcode){
		HttpURLConnection conn = null;
		BufferedReader in = null;
		String result = "";
		String pwd = "EAFABB28CACB6A5D2B827BA0663BFEF5";// MD5Util.MD5(USERNAME + PASSWORD);
		StringBuilder sb = new StringBuilder();
		sb.append("user=").append(USERNAME).append("&passwd=").append(pwd).append("&msg=").append(msgcode).append("&mobs=").append(mobile);
		try {
			URL url = new URL(URL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
 			conn.setRequestProperty("Content-Length",  String.valueOf(sb.toString()));
			conn.setDoInput(true);
			conn.connect();
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
			out.write(sb.toString());
			out.flush();
			out.close();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			log.info("短信接口返回:" + result);
		} catch (Exception e) {
			log.error(ExceptionUtil.getExceptionMessage(e));
		} finally  {
			try {
				if (in != null) {
					in.close();
				}
				conn.disconnect();
			} catch (IOException e) {
				log.error(ExceptionUtil.getExceptionMessage(e));
			}
        }
		return result;
	}
}