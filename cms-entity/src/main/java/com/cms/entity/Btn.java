package com.cms.entity;

import java.io.Serializable;

public class Btn implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String btnName;
	
	private String btnChsName;
	
	private Integer btnLevel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBtnName() {
		return btnName;
	}

	public void setBtnName(String btnName) {
		this.btnName = btnName;
	}

	public Integer getBtnLevel() {
		return btnLevel;
	}

	public void setBtnLevel(Integer btnLevel) {
		this.btnLevel = btnLevel;
	}

	public String getBtnChsName() {
		return btnChsName;
	}

	public void setBtnChsName(String btnChsName) {
		this.btnChsName = btnChsName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((btnChsName == null) ? 0 : btnChsName.hashCode());
		result = prime * result + ((btnLevel == null) ? 0 : btnLevel.hashCode());
		result = prime * result + ((btnName == null) ? 0 : btnName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Btn other = (Btn) obj;
		if (btnChsName == null) {
			if (other.btnChsName != null)
				return false;
		} else if (!btnChsName.equals(other.btnChsName))
			return false;
		if (btnLevel == null) {
			if (other.btnLevel != null)
				return false;
		} else if (!btnLevel.equals(other.btnLevel))
			return false;
		if (btnName == null) {
			if (other.btnName != null)
				return false;
		} else if (!btnName.equals(other.btnName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Btn [id=" + id + ", btnName=" + btnName + ", btnChsName=" + btnChsName + ", btnLevel=" + btnLevel + "]";
	}
}
