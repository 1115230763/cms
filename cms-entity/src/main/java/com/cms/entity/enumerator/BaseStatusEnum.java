package com.cms.entity.enumerator;

/**
 *  基础档案状态 base status
 */
public enum BaseStatusEnum {
	status0("启用_0"),
	status1("禁用_1");

	private final String status; 

	public String getStatus() {
		return status;
	}

	private BaseStatusEnum(String status) {  
		this.status = status;  
	}
}
