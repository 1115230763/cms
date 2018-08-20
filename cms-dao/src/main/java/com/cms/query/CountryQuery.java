package com.cms.query;

public class CountryQuery implements IQuery{
	private String countryName;
	private String countryEngName;

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

}
