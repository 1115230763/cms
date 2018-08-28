package com.cms.utils;

import java.io.IOException;
import java.util.Calendar;
 
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class QiniuUtils {
	private static final String ACCESSKEY  = ResourceUtil.getString("accessKey");
	private static final String SECRETKEY  = ResourceUtil.getString("secretKey");
	private static final String BUCKET  = ResourceUtil.getString("bucket");
	
	
	//上传文件
	public static String upload(MultipartFile file,String ext) throws IOException {
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone0()); 
		StringMap putPolicy = new StringMap();
		putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
		Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
		String upToken = auth.uploadToken(BUCKET);
		UploadManager uploadManager = new UploadManager(cfg);
		byte[] uploadBytes = file.getBytes();
		Response response = uploadManager.put(uploadBytes, ResourceUtil.getString("fileHead")+Calendar.getInstance().getTimeInMillis()+ext, upToken);
		//解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		return ResourceUtil.getString("domain")+putRet.key;
	}
	//几天后删除文件
	public static void deleteAfterDays(String key) throws QiniuException{
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone0());
		//过期天数，该文件10天后删除
		int days = 1;
		Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
		BucketManager bucketManager = new BucketManager(auth, cfg); 
		bucketManager.deleteAfterDays(BUCKET, key, days);
	}
	//直接删除文件
	public static void deleteBykey(String key) throws QiniuException{ 
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone0()); 
		Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		bucketManager.delete(BUCKET, key);
	}
	
}
