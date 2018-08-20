package com.cms.vo;

import com.cms.vo.iface.IDatagridVO;

public class BtnVO implements IDatagridVO {

	private java.lang.String id;
	private java.lang.String btnName;
	private java.lang.String btnChsName;
	private java.lang.Integer btnLevel;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getBtnName() {
		return btnName;
	}

	public void setBtnName(java.lang.String btnName) {
		this.btnName = btnName;
	}

	public java.lang.String getBtnChsName() {
		return btnChsName;
	}

	public void setBtnChsName(java.lang.String btnChsName) {
		this.btnChsName = btnChsName;
	}

	public java.lang.Integer getBtnLevel() {
		return btnLevel;
	}

	public void setBtnLevel(java.lang.Integer btnLevel) {
		this.btnLevel = btnLevel;
	}

}