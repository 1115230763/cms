package com.cms.entity;

import java.io.Serializable;

/**
 * @author 
 * @Date 
 */
public class Customer implements Serializable {

	private static final long serialVersionUID = -3565889243058539955L;

	private String id;

	private String customerName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", customerName=" + customerName + "]";
	}
	
}