package com.cms.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Exception工具類
 */
public class ExceptionUtil {

	/**
	 * 返回错误信息字串
	 * 
	 * @param Exception
	 * @return 错误信息字串
	 */
	public static String getExceptionMessage(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	public static String getRootCauseMessage(Exception e) {
		return ExceptionUtils.getRootCauseMessage(e);
	}
}
