package com.cms.entity;

import java.io.Serializable;
import java.util.Set;

/**
 * @author 
 * @Date 
 */
public class Role implements Serializable {

	private static final long serialVersionUID = 5570929059266063301L;

	private String id;

	private String roleName;

	private Set<Menu> menuSet;
	
	private Set<Btn> btnSet;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<Menu> getMenuSet() {
		return menuSet;
	}

	public void setMenuSet(Set<Menu> menuSet) {
		this.menuSet = menuSet;
	}

	public Set<Btn> getBtnSet() {
		return btnSet;
	}

	public void setBtnSet(Set<Btn> btnSet) {
		this.btnSet = btnSet;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", roleName=" + roleName + ", menuSet=" + menuSet + ", btnSet=" + btnSet + "]";
	}
}