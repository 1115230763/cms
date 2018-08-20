package com.cms.entity.enumerator;

/**
 * 	档案下载之content type
 */
public enum ContentTypeEnum {

	csv("text/csv;charset=utf-8"),
	html("text/html;charset=utf-8"),
	txt_big5("text/plain;charset=Big5"),
	txt("text/plain;charset=utf-8"),
	png("image/png;charset=utf-8"),
	gif("image/gif;charset=utf-8"),
	jpg("image/jpeg;charset=utf-8"),
	jpeg("image/jpeg;charset=utf-8"),
	jpe("image/jpeg;charset=utf-8"),
	ppt("application/powerpoint;charset=utf-8"),
	xls("application/msexcel;charset=utf-8"),
	doc("application/msword;charset=utf-8"),
	pdf("application/pdf;charset=utf-8"),
	rtf("application/rtf;charset=utf-8"),
	download("application/x-msdownload;charset=utf-8"),
	stream("application/octet-stream;charset=utf-8");
	private final String contentType; 
	
	public String getContentType(){
   	 	return this.contentType;
    }
	
	private ContentTypeEnum(String contentType) {  
		this.contentType = contentType;  
	} 
}
