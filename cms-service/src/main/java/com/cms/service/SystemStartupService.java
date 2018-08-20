package com.cms.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

/**
 * 系统启动时执行
 */
@Service("systemStartupService")
public class SystemStartupService {
	
	@PostConstruct
	public void init(){
		System.out.println("系统启动執行一次");
	}
	
}
