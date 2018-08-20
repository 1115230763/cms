package com.cms.utils.comparator;

import java.util.Comparator;

import com.cms.entity.Menu;

/**
 * Menu Comparator
 */
public class MenuComparator implements Comparator<Menu>{
	
	@Override
	public int compare(Menu o1, Menu o2) {
		int i1 = o1.getDisplaySeq() != null ? o1.getDisplaySeq().intValue() : -1;
		int i2 = o2.getDisplaySeq() != null ? o2.getDisplaySeq().intValue() : -1;
		return i1 - i2;
	}

}
