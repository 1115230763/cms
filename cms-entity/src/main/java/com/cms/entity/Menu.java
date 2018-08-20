package com.cms.entity;

import java.io.Serializable;
import java.util.Set;

public class Menu implements Serializable{

	private static final long serialVersionUID = 4823470055198921348L;

	private String id;

	private String menuName;
	
	private String menuType;
	
	private String url;
	
	private String parentId;
	
	private Integer displaySeq;
	
	private Set<Role> roleSet;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getDisplaySeq() {
		return displaySeq;
	}

	public void setDisplaySeq(Integer displaySeq) {
		this.displaySeq = displaySeq;
	}

	public Set<Role> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", menuName=" + menuName + ", menuType=" + menuType + ", url=" + url + ", parentId="
				+ parentId + ", displaySeq=" + displaySeq + ", roleSet=" + roleSet + "]";
	}
}