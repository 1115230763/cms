package com.cms.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.cms.utils.vo.BaiduMapVO;

public class MapUtil {
	private static final Logger logger = Logger.getLogger(MapUtil.class);
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
	private static final double EARTH_RADIUS = 6378.137;   
	private static final String BAIDU_MAP_URL = "http://api.map.baidu.com/place/v2/search?region=289&output=json&ak=%s&query=%s";
	private static final String BAIDU_MAP_URL_DISTANCE = "http://api.map.baidu.com/direction/v1?mode=driving&origin=%s&destination=%s&origin_region=%s&destination_region=%s&output=json&ak=%s&tactics=12";
	private static final String BAIDU_KEY = "UHB4P11h0ffBpYodT8j0b3Et";
	
	/**
	 * 计算时速
	 * @param dataList
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal calculateSpeedPerHour(List<BaiduMapVO> dataList) {
		BigDecimal speedPerHour = new BigDecimal("0");
		try {
			int index = dataList.size() - 1;
			double distance = 0;
			for(int i = index ; i > 0 ; i--){
				distance = distance + MapUtil.GetDistance(
					Double.parseDouble(dataList.get(i).getLat()), 
					Double.parseDouble(dataList.get(i).getLng()), 
					Double.parseDouble(dataList.get(i-1).getLat()), 
					Double.parseDouble(dataList.get(i-1).getLng())
				);
				index--;
			}
			Date beginDate= SDF.parse(dataList.get(0).getTime());   
			Date endDate= SDF.parse(dataList.get(dataList.size() - 1).getTime());   
			double hour = new Double((endDate.getTime() - beginDate.getTime())) / new Double(60 * 60 * 1000);  

			if(hour > 0){
				speedPerHour = new BigDecimal(Double.toString(distance / hour)).setScale(2, BigDecimal.ROUND_HALF_UP);
				System.out.println("距离(公里) = " + distance);
				System.out.println("小时 = " + hour);  
				System.out.println("时速 = " + speedPerHour);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		return speedPerHour;
	}
	
	private static double rad(double d) {   
	     return d * Math.PI / 180.0;   
	}    
	  
	/**   
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为公里   
	 */   
	public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {   
	    double radLat1 = rad(lat1);   
	    double radLat2 = rad(lat2);   
	    double a = radLat1 - radLat2;   
	    double b = rad(lng1) - rad(lng2);   
	    double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b/2),2)));   
	    s = s * EARTH_RADIUS / 1000;  
	    return s;   
	}   
	
	/**
	 *  利用两点经纬度计算路程公里数
	 * @param startPosition
	 * @param endPosition
	 * @param startCity
	 * @param endCity
	 * @return
	 * @throws Exception
	 */
	public static Double GetRouteDistanceByBaidu(String startPosition, String endPosition, String startCity, String endCity) throws Exception {
		URLConnection connection = null;
		try {
			connection = new URL(String.format(BAIDU_MAP_URL_DISTANCE, URLEncoder.encode(startPosition, "UTF-8"), URLEncoder.encode(endPosition, "UTF-8"), URLEncoder.encode(startCity, "UTF-8"), URLEncoder.encode(endCity, "UTF-8"), BAIDU_KEY)).openConnection();
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
		} 
		
		Double distance = 0.00;
		if (connection != null) {
			StringBuilder sb = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
				String data = null;
				while ((data = reader.readLine()) != null) {
					sb.append(data);
				}
				if(sb != null && sb.length() > 0 && sb.toString().indexOf("distance") != -1){
					distance = Double.parseDouble(sb.substring(sb.indexOf("distance"), sb.indexOf("distance") + 100).substring(sb.substring(sb.indexOf("distance"), sb.indexOf("distance") + 100).indexOf(":") + 1, sb.substring(sb.indexOf("distance"), sb.indexOf("distance") + 100).indexOf(",")))/1000;
				}
			} catch (Exception e) {
				logger.error(ExceptionUtil.getExceptionMessage(e));
			}
		}
		return distance;
	}
	
	/**
	 * 利用部分地名查询地址列表
	 * @param addr
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Set<BaiduMapVO> queryAddress(String addr) {
		BaiduMapVO vo = null;
		URLConnection connection = null;
		Set<BaiduMapVO> baiduPlaceSet = new HashSet<BaiduMapVO>();
		try {
			connection = new URL(String.format(BAIDU_MAP_URL, BAIDU_KEY, URLEncoder.encode(addr, "UTF-8"))).openConnection();
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
		} 
		
		if (connection != null) {
			StringBuilder sb = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
				String data = null;
				while ((data = reader.readLine()) != null) {
					sb.append(data);
				}
				if(sb != null && sb.length() > 0){
					Map<String, Object> obj = new ObjectMapper().readValue(sb.toString(), Map.class);
					List<LinkedHashMap> results = (List<LinkedHashMap>)obj.get("results");
					for(LinkedHashMap result : results){
						if(result.get("location") != null && 
								StringUtils.isNotEmpty(result.get("location").toString()) &&
									result.get("location").toString().indexOf(",") > 0 && 
										result.get("location").toString().indexOf("=") > 0 ){
							vo = new BaiduMapVO();
							vo.setName(result.get("name").toString());
							vo.setAddress(result.get("address").toString());
							vo.setLat(result.get("location").toString().replaceAll(" ", "").substring(1, result.get("location").toString().length()-2).split(",")[0].split("=")[1]);
							vo.setLng(result.get("location").toString().replaceAll(" ", "").substring(1, result.get("location").toString().length()-2).split(",")[1].split("=")[1]);
							
							baiduPlaceSet.add(vo);
						}
					}
				}
			} catch (Exception e) {
				logger.error(ExceptionUtil.getExceptionMessage(e));
			}
		}
		return baiduPlaceSet;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(MapUtil.GetRouteDistanceByBaidu("31.301717,121.348785", "31.223334,121.479681", "上海", "上海"));
	}
}