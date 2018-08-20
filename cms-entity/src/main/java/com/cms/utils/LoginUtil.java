package com.cms.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cms.entity.UserLogin;
import com.cms.utils.ResourceUtil;

public class LoginUtil {

	public static UserLogin getLoginUser(){
		return (UserLogin) ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession().getAttribute(ResourceUtil.getUserInfo());
	}
}
