package com.cms.interceptor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.cms.utils.RandomUtil;

public class ServiceLogInterceptor implements MethodInterceptor{
	private static final Logger logger = Logger.getLogger("service_log");
	private static final Level LOG_LEVEL_METHOD = Level.INFO;
	
	@SuppressWarnings("unchecked")
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = null;
		long startTime = System.currentTimeMillis();
		StringBuilder info = new StringBuilder();
		String uuid = RandomUtil.getUUID();
		try {
			logger.log(LOG_LEVEL_METHOD, info.append("==========================").append("Start，执行ID：Service-").append(uuid).append("=============================").toString());info.setLength(0);
			logger.log(LOG_LEVEL_METHOD, info.append("执行:").append(invocation.getMethod()).toString());info.setLength(0);
			Object[] args = invocation.getArguments();
			info.append("\n");
			if (args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					info.append("\t\t").append("执行参数:[" + i + "] = ");
					if (nonVO(args[i])) {
						info.append(args[i]);
					} else {
						info.append(this.reflectObjToStr(args[i]));
					}
					if (i < args.length - 1) {
						info.append("\n");
					}
				}
			}else{
				info.append("\t\t").append("无输入参数");
			}
			logger.log(LOG_LEVEL_METHOD, info.toString());info.setLength(0);
			
			result = invocation.proceed();
			if (result != null) {
				info.append("\n").append("\t\t").append("回传結果 = ");
				if (nonVO(result)) {
					if (result instanceof List) {
						List<Object> resultList = (List<Object>) result;
						info.append(resultList.getClass()).append(", [size]=").append(resultList.size()).append("\n");
						if (resultList != null && resultList.size() > 10) {
							for (Object obj : resultList.subList(0, 5)) {
								info.append("\t\t\t").append(this.reflectObjToStr(obj)).append("\n");
							}
							info.append("\t\t\t总资料长度为").append(resultList.size()).append("，只显示前5笔资料");
						} else {
							info.append("\t\t\t").append(result);
						}
					} else {
						info.append(result);
					}
				} else {
					info.append(this.reflectObjToStr(result));
				}
				logger.log(LOG_LEVEL_METHOD, info.toString());info.setLength(0);
			}
		} finally {
			logger.log(LOG_LEVEL_METHOD, info.append("执行时间：").append(System.currentTimeMillis() - startTime).append(" ms.").toString());info.setLength(0);
			logger.log(LOG_LEVEL_METHOD, info.append("==========================").append("Finish，执行ID：Service-").append(uuid).append("=============================").toString());info.setLength(0);
		}
		return result;
	}
	
	/**
	 * 取得物件內容字串     
	 * @param obj : Object 物件     
	 * @return String 物件內容字串
	 *    
	 */
	private String reflectObjToStr(Object obj) {
		return reflectObjToStr(obj, ToStringStyle.DEFAULT_STYLE);
	}

	/**
	 * 取得物件內容字串     
	 * @param obj   : Object 物件
 	 * @param style :
	 * @return String 物件內容字串    
	 */
	private String reflectObjToStr(Object obj, ToStringStyle style) {
		if (obj == null) {
			return "";
		} else {
			String result = ToStringBuilder.reflectionToString(obj, style);
			return result;
		}
	}

	private boolean nonVO(Object object) {
		boolean isVO = false;

		if (object instanceof String || object instanceof Boolean
				|| object instanceof Date || object instanceof BigDecimal
				|| object instanceof Double || object instanceof Integer
				|| object instanceof Map || object instanceof List) {
			isVO = true;
		}
		return isVO;
	}

	
}
