package com.cms.dao;


import java.util.List;

import com.cms.dao.BaseDao;
import com.cms.entity.Menu;
import com.cms.query.MenuQuery;
/**
 * 
 * <br>
 * <b>功能：</b>MenuDao<br>
 */
public interface MenuDao extends BaseDao {

	public Menu queryRoleListById(MenuQuery menuQuery);

	public void addRoleByMenu(Menu menu);

	public void deleteRoleByMenu(Menu menu);

	public List<Menu> queryByParentId(MenuQuery menuQuery);
	
	
}
