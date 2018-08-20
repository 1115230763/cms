package com.cms.utils.comparator;

import java.util.Comparator;

import com.cms.entity.UserLogin;

public class UserLoginComparator implements Comparator<UserLogin> {

	@Override
	public int compare(UserLogin o1, UserLogin o2) {
		return o1.getId().compareToIgnoreCase(o2.getId());
	}


}
