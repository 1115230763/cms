package com.cms.easyui;

import java.util.Map;
import java.util.Set;

/**
 * easyui使用的tree物件
 */
public class EasyuiTree implements Comparable<EasyuiTree> {
	
	private String id;
	private String text;// 节点名称
	private String iconCls;// 小圆标
	private Boolean checked = false;// 是否勾选状态
	private Map<String, Object> attributes;// 其他参数
	private Set<EasyuiTree> children;// 子节点
	private String state = "open";// 是否展开(open,closed)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}


	public Set<EasyuiTree> getChildren() {
		return children;
	}

	public void setChildren(Set<EasyuiTree> children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		EasyuiTree other = (EasyuiTree) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(EasyuiTree other) {
		int value = 0;
		if(attributes != null){
			int i1 = attributes.get("seq") != null ? (Integer)attributes.get("seq") : -1;
			int i2 = other.getAttributes().get("seq") != null ? (Integer) other.getAttributes().get("seq") : -1;
			value = i1 - i2;
		}else{
			value = id.compareToIgnoreCase(other.getId());
		}
		return value;
	}

	@Override
	public String toString() {
		return "EasyuiTree [id=" + id + ", text=" + text + ", iconCls=" + iconCls + ", checked=" + checked + ", attributes=" + attributes + ", children=" + children + ", state=" + state + "]";
	}
}
