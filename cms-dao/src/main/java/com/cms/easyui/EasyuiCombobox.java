package com.cms.easyui;

public class EasyuiCombobox {
	
	private String id;
	private String value;
	private Object option;
	private boolean selected;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Object getOption() {
		return option;
	}

	public void setOption(Object option) {
		this.option = option;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String toString(){
        return "id :"+id+"  "+"value:"+value;
    }
    public boolean equals(Object obj){
         if(obj instanceof EasyuiCombobox){
         EasyuiCombobox easyuiCombobox=(EasyuiCombobox)obj;
         return (id.equals(easyuiCombobox.id))&&(value.equals(easyuiCombobox.value));
         }
         return super.equals(obj);
     }
     public int hashCode(){
         return id.hashCode();
     }
}
