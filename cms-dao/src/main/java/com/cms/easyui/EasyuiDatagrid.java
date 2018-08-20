package com.cms.easyui;

import java.util.List;

/**
 * EasyUI Datagrid JSON object
 */
public class EasyuiDatagrid<T> {
	
	private Long total;// 总笔数
	private List<T> rows;// 全部资料
	private List<T> footer;// 全部资料
	private boolean success = true;// 是否成功
	private Object obj = null;// 其他信息

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public List<T> getFooter() {
		return footer;
	}

	public void setFooter(List<T> footer) {
		this.footer = footer;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
