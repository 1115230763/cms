package com.cms.dao;


import java.util.List;

import com.cms.dao.BaseDao;
import com.cms.entity.Role;
import com.cms.query.RoleQuery;
/**
 * 
 * <br>
 * <b>功能：</b>RoleDao<br>
 */
public interface RoleDao extends BaseDao {

	public Role queryBtnListById(RoleQuery roleQuery);
	
	public Role queryMenuListById(RoleQuery roleQuery);

	public Role queryUniqueIdByName(RoleQuery roleQuery);

	public Long queryByCount();

	public List<Role> queryRoleListByAll();

	public void addBtnByRole(Role role);

	public void deleteBtnByRole(Role role);

}
