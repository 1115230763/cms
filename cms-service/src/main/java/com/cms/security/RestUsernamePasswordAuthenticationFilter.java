package com.cms.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		boolean retVal = false;
		String username = request.getParameter(this.getUsernameParameter());
		String password = request.getParameter(this.getPasswordParameter());
		if (username != null && password != null) {
			Authentication authResult = null;
			try {
				authResult = attemptAuthentication(request, response);
				if (authResult == null) {
					retVal = false;
				}
			} catch (AuthenticationException failed) {
				try {
					unsuccessfulAuthentication(request, response, failed);
				} catch (Exception e) {
					retVal = false;
				}
				retVal = false;
			}
			try {
				successfulAuthentication(request, response, null, authResult);
			} catch (Exception e) {
				retVal = false;
			}
			return false;
		} else {
			retVal = true;
		}
		return retVal;
	}
}
