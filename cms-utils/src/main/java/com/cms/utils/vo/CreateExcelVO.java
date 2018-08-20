package com.cms.utils.vo;

import java.io.OutputStream;
import java.util.List;

public class CreateExcelVO {
	private long maxRowPerPage;
	private String sheetNamePrefix;
	private String[] columnName;
	private OutputStream outputStream;
	private  List<String[]> dataList;
	
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public List<String[]> getDataList() {
		return dataList;
	}
	public void setDataList(List<String[]> dataList) {
		this.dataList = dataList;
	}
	public String[] getColumnName() {
		return columnName;
	}
	public void setColumnName(String[] columnName) {
		this.columnName = columnName;
	}
	public long getMaxRowPerPage() {
		return maxRowPerPage;
	}
	public void setMaxRowPerPage(long maxRowPerPage) {
		this.maxRowPerPage = maxRowPerPage;
	}
	public String getSheetNamePrefix() {
		return sheetNamePrefix;
	}
	public void setSheetNamePrefix(String sheetNamePrefix) {
		this.sheetNamePrefix = sheetNamePrefix;
	}
}
