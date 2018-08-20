package com.cms.utils.vo;

import java.util.List;
import java.util.Map;

public class MailVO {
	private String to;
	private String subject;
	private String content;
	private Map<String, String> attchFileData;
	private List<InputStreamDataVO> attchInputStreamDataList;

	public List<InputStreamDataVO> getAttchInputStreamDataList() {
		return attchInputStreamDataList;
	}

	public void setAttchInputStreamDataList(List<InputStreamDataVO> attchInputStreamDataList) {
		this.attchInputStreamDataList = attchInputStreamDataList;
	}
	
	public Map<String, String> getAttchFileData() {
		return attchFileData;
	}

	public void setAttchFileData(Map<String, String> attchFileData) {
		this.attchFileData = attchFileData;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
