package com.cms.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.cms.utils.ExceptionUtil;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private static final Logger logger = Logger.getLogger(RestAuthenticationEntryPoint.class);
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		if(authException !=null && ExceptionUtil.getExceptionMessage(authException).length() > 0){
			StringBuilder sb = new StringBuilder();
			sb.append("使用者：").append(request.getParameter("username")).append(",").append("webservice 调用失败，失败原因：")
			  .append(ExceptionUtil.getExceptionMessage(authException));
			logger.error(sb.toString());
		}
	}
}
