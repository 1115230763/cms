package com.cms.security;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class RestCsrfSecurityRequestMatcher implements RequestMatcher {

	private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
	
	@Override
	public boolean matches(HttpServletRequest request) {
		if (excludeUrls != null && excludeUrls.size() > 0) {
			String servletPath = request.getServletPath();
			for (String url : excludeUrls) {
				if (servletPath.contains(url)) {
					return false;
				}
			}
		}
		return !allowedMethods.matcher(request.getMethod()).matches();
	}

	private List<String> excludeUrls;

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}
}
