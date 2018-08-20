package com.cms.utils;

import org.apache.commons.lang.StringUtils;

public class CustomerContextHolder {
    public static final String DATA_SOURCE_WMSSQL = "dataSourceOne";
    public static final String DATA_SOURCE_MMCSQL = "dataSourceTwo";
    //用ThreadLocal来设置当前线程使用哪个dataSource
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    public static void setCustomerType(String customerType) {
    	System.out.println("切换数据库连接：" + customerType);
        contextHolder.set(customerType);
    }
    public static String getCustomerType() {
        String dataSource = contextHolder.get();
        if (StringUtils.isEmpty(dataSource)) {
            return DATA_SOURCE_WMSSQL;
        }else {
        	System.out.println("切换数据库连接：" + dataSource);
            return dataSource;
        }
    }
    public static void clearCustomerType() {
        contextHolder.remove();
    }
}
