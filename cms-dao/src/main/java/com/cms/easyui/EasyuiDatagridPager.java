package com.cms.easyui;

/**
 * 設定 EasyUI Datagrid 基本資料
 */
public class EasyuiDatagridPager {
	
	private int page = 1;//起始页
	private int rows = 10;//每页显示资料笔数
	private String countBy = "id";//计数栏位名称
 
	
	private String order;
	
	private String sort;
	
	
	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getRows() {
		return rows;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public String getCountBy() {
		return countBy;
	}
	
	public void setCountBy(String countBy) {
		this.countBy = countBy;
	}
	 
	
}
