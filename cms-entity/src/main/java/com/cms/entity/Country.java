package com.cms.entity;

import java.io.Serializable;

/**
 * @author 
 * @Date 
 */
public class Country implements Serializable {

	private static final long serialVersionUID = -291431069173425720L;

	private Integer id;

	private String countryName;
	
	private String countryEngName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryEngName() {
		return countryEngName;
	}

	public void setCountryEngName(String countryEngName) {
		this.countryEngName = countryEngName;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", countryName=" + countryName + ", countryEngName=" + countryEngName + "]";
	}

}