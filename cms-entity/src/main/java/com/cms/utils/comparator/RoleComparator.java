package com.cms.utils.comparator;

import java.util.Comparator;

import com.cms.entity.Role;

public class RoleComparator implements Comparator<Role> {

	@Override
	public int compare(Role o1, Role o2) {
		return o1.getId().compareToIgnoreCase(o2.getId());
	}

}
