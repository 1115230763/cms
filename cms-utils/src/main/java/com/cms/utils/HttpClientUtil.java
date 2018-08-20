package com.cms.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


public class HttpClientUtil {

	public static String send(String url, String body) throws Exception {
        
		HttpClient client = HttpClientBuilder.create().build();
		HttpPut post = new HttpPut(url);
		HttpEntity entity = new StringEntity(body, "utf-8");
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			String resEntityStr = EntityUtils.toString(response.getEntity());
			return resEntityStr;
			//return new String(resEntityStr.getBytes("iso-8859-1"), "utf-8");
		} else if(response.getStatusLine().getStatusCode() == 404){
    		throw new Exception("报错~~");
    	} else {
    		throw new Exception();
    	}
	}

	public static String getPost(String url, String body) throws Exception {
        
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		HttpEntity entity = new StringEntity(body, "utf-8");
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			String resEntityStr = EntityUtils.toString(response.getEntity());
			return resEntityStr;
			//return new String(resEntityStr.getBytes("iso-8859-1"), "utf-8");
		} else if(response.getStatusLine().getStatusCode() == 404){
    		throw new Exception("报错~~");
    	} else {
    		throw new Exception();
    	}
	}
	public static String getSend(String url, String body) throws Exception {
        
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet post = new HttpGet(url); 
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			String resEntityStr = EntityUtils.toString(response.getEntity());
			return resEntityStr;
			//return new String(resEntityStr.getBytes("iso-8859-1"), "utf-8");
		} else if(response.getStatusLine().getStatusCode() == 404){
    		throw new Exception("报错~~");
    	} else {
    		throw new Exception();
    	}
	}
	public static void main(String[] args) throws Exception {
		System.out.println(getSend("http://cms-test8.mamachong.com/index.php?c=Api&a=purchase_order_lock&lock_state=lock&id=CG1807191147379467",null));
		//System.out.println(getSend("http://cms-test8.mamachong.com/index.php?c=Api&a=purchase_order_lock&lock_state=unlock&id=14532453245234523452345234",null));
		//System.out.println(getPost("http://cms.mmchong.com/index.php?c=Api&a=purchase_order_edit&purchase_id=10262&purchase_order_id=46771&goods_id=064a63cc-75eb-11e8-bc22-00163e048ad2&goods_num=-1",""));
		
	}
}