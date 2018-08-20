package com.cms.security;

import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.cms.utils.EncryptUtil;

public class RestEncoder implements PasswordEncoder {

	@Override
	public String encodePassword(String origPwd, Object salt) {
		return EncryptUtil.md5AndSha(origPwd);
	}

	@Override
	public boolean isPasswordValid(String encPwd, String origPwd, Object salt) {
		return encPwd.equals(encodePassword(origPwd, salt));
	}
}
