package com.cms.utils;

import java.io.File;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 参数工具类
 */
public class ResourceUtil {

	private static final ResourceBundle configBundle = ResourceBundle.getBundle("config");
	
	public static final String getString(String key){
		return configBundle.getString(key);
	}
	
	public static final String geteEndpoint(){
		return configBundle.getString("endpoint");
	}
	
	public static final String getMailPwdTemplate(){
		return configBundle.getString("mail.sendPwd");
	}
	
	public static final String getBarcodeXmlRootPath(){
		StringBuilder sb = new StringBuilder();
		sb.append(ResourceUtil.class.getClassLoader().getResource("").getPath()).append(ResourceUtil.getFileSeparator())
		.append(configBundle.getString("barcodeXmlRootPath")).append(ResourceUtil.getFileSeparator());
		return sb.toString();
	}
	
	public static String getImportRootPath(String filename) {
		StringBuilder sb = new StringBuilder();
		sb.append(ResourceUtil.class.getClassLoader().getResource("").getPath()).append(ResourceUtil.getFileSeparator())
		  .append("template").append(ResourceUtil.getFileSeparator())
		  .append(configBundle.getString("importTemplateRootPath")).append(ResourceUtil.getFileSeparator())
		  .append(filename);
		return URLDecoder.decode(sb.toString());
	}
	
	/**
	 * word template root path
	 * @return
	 */
	public static final String getWordTemplateRootPath(){
		return configBundle.getString("wordTemplateRootPath");
	}
	
	/**
	 * pdf template root path
	 * @return
	 */
	public static final String getPdfTemplateRootPath(){
		StringBuffer sb = new StringBuffer();
		String[] dataArray = configBundle.getString("pdfTemplateRootPath").split("/");
		for(String data : dataArray){
			sb.append(data).append(ResourceUtil.getFileSeparator());
		}
		return sb.toString();
	}
	/**
	 * mp3 root path
	 * @return
	 */
	public static final String getMp3RootPath(){
		StringBuffer sb = new StringBuffer();
		String[] dataArray = configBundle.getString("mp3RootPath").split("/");
		for(String data : dataArray){
			sb.append(data).append(ResourceUtil.getFileSeparator());
		}
		return sb.toString();
	}
	/**
	 * 取得REST定义帐号参数名称
	 * @return
	 */
	public static final String getRESTLoginName() {
		return configBundle.getString("usernameParameter");
	}
	
	/**
	 * 取得REST定义密码参数名称
	 * @return
	 */
	public static final String getRESTLoginPassword() {
		return configBundle.getString("passwordParameter");
	}
	
	/**
	 * 取得gmail帳號
	 * @return
	 */
	public static final String getMailUser(){
		return configBundle.getString("mail.username");
	}
	
	/**
	 * 取得gmail密碼
	 * @return
	 */
	public static final String getMailPassword(){
		return configBundle.getString("mail.password");
	}
	
	/**
	 * 取得sessionInfo名字
	 * @return
	 */
	public static final String getUserInfo() {
		return configBundle.getString("userInfo");
	}

	public static final String getFileUploadSizeLimit() {
		return configBundle.getString("fileUploadSizeLimit");
	}
	
	public static String getProcessResultMsg(boolean result){
		String resultMsg = null;
		if(result){
			resultMsg = "资料处理成功！";
		}else{
			resultMsg = "资料处理失败！";
		}
		return resultMsg;
	}

	/**
	 * 取得檔案分隔符號
	 * @return
	 */
	public static final String getFileSeparator() {
		return File.separator;
	}

	/**
	 * 上传图档超过限制大小的错误信息
	 * @param ex
	 * @return
	 */
	public static String getFileUploadErrMsg(MaxUploadSizeExceededException ex) {
		StringBuilder sb = new StringBuilder();
		DecimalFormat df = new DecimalFormat("######0.0"); 
		
		Pattern pattern = Pattern.compile("\\d{2,}");//这个2是指连续数字的最少个数
        String errMsg = ExceptionUtil.getRootCauseMessage(ex);
        Matcher matcher = pattern.matcher(errMsg);
        matcher.find();
        double fileSize = Double.parseDouble(matcher.group());
		sb.append("上传图档大小为").append(df.format(fileSize/1024/1024)).append("MB，超过系统限制 ").append(ResourceUtil.getFileUploadSizeLimit());
		return sb.toString();
	}
}
