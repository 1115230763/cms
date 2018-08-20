package com.cms.product.datagrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cms.dao.RoleDao;
import com.cms.easyui.EasyuiDatagrid;
import com.cms.easyui.EasyuiDatagridPager;
import com.cms.entity.Menu;
import com.cms.entity.Role;
import com.cms.product.abst.AbstDatagrid;
import com.cms.query.IQuery;
import com.cms.utils.BeanUtils;
import com.cms.utils.comparator.MenuComparator;
import com.cms.vo.RoleVO;
import com.cms.vo.iface.IDatagridVO;

public class SysRoleDatagrid extends AbstDatagrid {

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public EasyuiDatagrid<IDatagridVO> createDatagrid(EasyuiDatagridPager pager, IQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EasyuiDatagrid<IDatagridVO> createDatagrid() {
		RoleVO roleVO = null;
		List<Menu> menuList = null;
		
		List<IDatagridVO> roleVOList = new ArrayList<>();
		EasyuiDatagrid<IDatagridVO> datagrid = new EasyuiDatagrid<>();
		
		List<Role> roleList = roleDao.queryByAll();
		for (Role role : roleList) {
			roleVO = new RoleVO();
			menuList = new ArrayList<Menu>();
			BeanUtils.copyProperties(role, roleVO);
			menuList.addAll(role.getMenuSet());
			Collections.sort(menuList, new MenuComparator());
			roleVO.setMenuList(menuList);
			roleVOList.add(roleVO);
		}
		datagrid.setTotal(roleDao.queryByCount());
		datagrid.setRows(roleVOList);
		return datagrid;
	}

	@Override
	public EasyuiDatagrid<IDatagridVO> createDatagrid(IQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

}
