package com.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cms.dao.RoleDao;
import com.cms.dao.UserDao;
import com.cms.entity.Btn;
import com.cms.entity.Role;
import com.cms.entity.User;
import com.cms.entity.UserLogin;
import com.cms.query.RoleQuery;
import com.cms.vo.Json;

/**
 * 	基础Service
 */
@Service("baseService")
public class BaseService {
	@Autowired
	private UserDao userMybatisDao;
	@Autowired
	private RoleDao roleMybatisDao;
	
	@Cacheable(value = "btnCache", key = "#userLogin.id+#menuId")
	public Json getBtn(String menuId, UserLogin userLogin) {
		Json json = new Json();
		StringBuilder sb = new StringBuilder();
		if(userLogin != null){
			User user = userMybatisDao.queryListById(userLogin);
			for(Role role : user.getRoleSet()){
				RoleQuery roleQuery = new RoleQuery();
				roleQuery.setId(role.getId());
				Role subRole = roleMybatisDao.queryBtnListById(roleQuery);
				for(Btn btn : subRole.getBtnSet()){
					sb.append(btn.getBtnName()).append(",");
				}
			}
			
			if(sb.length() > 0 && sb.lastIndexOf(",") > -1){
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			json.setSuccess(true);
			json.setObj(sb.toString());
		}else{
			json.setSuccess(false);
		}
		return json;
	}
}
