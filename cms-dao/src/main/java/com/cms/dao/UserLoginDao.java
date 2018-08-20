package com.cms.dao;

import com.cms.dao.BaseDao;
import com.cms.entity.UserLogin;
import com.cms.query.UserLoginQuery;

public interface UserLoginDao extends BaseDao {

	public UserLogin queryAppLoginById(UserLoginQuery userLoginQuery);
	
}
