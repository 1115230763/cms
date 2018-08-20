package com.cms.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class BeanConvertUtil {
	private static final Logger logger = Logger.getLogger(BeanConvertUtil.class);
	
	public static Map<String, Object> bean2Map(Object obj) {  
        Map<String, Object> map = new HashMap<String, Object>();  
        try {  
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
            for (PropertyDescriptor property : propertyDescriptors) {  
                String key = property.getName();  
                if (!key.equals("class")) {  
                    Method getter = property.getReadMethod();  
                    Object value = getter.invoke(obj);  
                    map.put(key, value);  
                }  
            }  
        } catch (Exception e) {  
            logger.error("transBean2Map Error " + e);
        }  
        return map;  
    }
	
	/**
	 * @param keyOrder 自定义Key顺序
	 * @param objList
	 * @return
	 */
	public static List<String[]> ListObj2ListStringArray(String[] keyOrder, List<?> objList) { 
		List<String[]> result = new ArrayList<String[]>();
		try {
			Map<String, Object> map = null;
			String[] dataArray = null;
			Object[] objArray = objList.toArray();
			for(Object obj : objArray){
				map = BeanConvertUtil.bean2Map(obj);
				if (map != null && map.keySet().size() > 0) {
					int i = 0;
					if(keyOrder != null && keyOrder.length > 0){
						dataArray = new String[keyOrder.length];
						for (String key : keyOrder) {
							dataArray[i] = map.get(key) != null ? map.get(key).toString() : "";
							i++;
						}
					}else{
						dataArray = new String[map.keySet().size()];
						for (String key : map.keySet()) {
							dataArray[i] = map.get(key) != null ? map.get(key).toString() : "";
							i++;
						}
					}
				} 
				if(dataArray != null && dataArray.length > 0){
					result.add(dataArray);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		return result;  
    }
}
