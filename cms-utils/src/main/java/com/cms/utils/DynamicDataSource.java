package com.cms.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
		System.out.println("<=================================>");
		System.out.println(CustomerContextHolder.getCustomerType());
        return CustomerContextHolder.getCustomerType();
    }
}
