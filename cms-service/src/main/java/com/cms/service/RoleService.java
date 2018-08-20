package com.cms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cms.dao.BtnDao;
import com.cms.dao.RoleDao;
import com.cms.easyui.EasyuiCombobox;
import com.cms.easyui.EasyuiDatagrid;
import com.cms.entity.Btn;
import com.cms.entity.Menu;
import com.cms.entity.Role;
import com.cms.query.BtnQuery;
import com.cms.query.RoleQuery;
import com.cms.utils.comparator.MenuComparator;
import com.cms.vo.Json;
import com.cms.vo.RoleVO;
import com.cms.vo.form.RoleForm;

@Service("roleService")
public class RoleService extends BaseService {

	@Autowired
	private RoleDao roleMybatisDao;
	@Autowired
	private BtnDao btnMybatisDao;
	
	public EasyuiDatagrid<RoleVO> getRoleDatagrid() {
		EasyuiDatagrid<RoleVO> datagrid = new EasyuiDatagrid<RoleVO>();
		List<Role> roleList = roleMybatisDao.queryRoleListByAll();
		List<RoleVO> roleVOList = new ArrayList<RoleVO>();
		List<Menu> menuList = null;
		RoleVO roleVO = null;
		for (Role role : roleList) {
			System.out.println(role.getMenuSet());
			roleVO = new RoleVO();
			menuList = new ArrayList<Menu>();
			BeanUtils.copyProperties(role, roleVO);
			menuList.addAll(role.getMenuSet());
			Collections.sort(menuList, new MenuComparator());
			roleVO.setMenuList(menuList);
			
			roleVOList.add(roleVO);
		}
		datagrid.setTotal(roleMybatisDao.queryByCount());
		datagrid.setRows(roleVOList);
		return datagrid;
	}

	@CacheEvict(value = "btnCache", allEntries = true)
	@Transactional
	public Json addRole(RoleForm roleForm) {
		Json json = new Json();
		Role role = new Role();
		BeanUtils.copyProperties(roleForm, role);
		Set<Btn> btnSet = null;
		if(StringUtils.isNotEmpty(roleForm.getBtns())){
			btnSet = new HashSet<Btn>();
			String[] btnIdArray = roleForm.getBtns().split(",");
			for(String btnId : btnIdArray){
				BtnQuery btnQuery = new BtnQuery();
				btnQuery.setId(btnId);
				btnSet.add((Btn) btnMybatisDao.queryById(btnQuery));
			}
		}
		role.setBtnSet(btnSet);
		
		roleMybatisDao.add(role);
		if(StringUtils.isNotEmpty(roleForm.getBtns())){
			roleMybatisDao.deleteBtnByRole(role);
			roleMybatisDao.addBtnByRole(role);
		}
		json.setSuccess(true);
		return json;
	}

	@CacheEvict(value = "btnCache", allEntries = true)
	@Transactional
	public Json editRole(RoleForm roleForm) {
		Json json = new Json();
		RoleQuery roleQuery = new RoleQuery();
		roleQuery.setId(roleForm.getRoleId());
		Role role = roleMybatisDao.queryBtnListById(roleQuery);
		BeanUtils.copyProperties(roleForm, role);
		Set<Btn> btnSet = null;
		if(StringUtils.isNotEmpty(roleForm.getBtns())){
			btnSet = new HashSet<Btn>();
			String[] btnIdArray = roleForm.getBtns().split(",");
			for(String btnId : btnIdArray){
				BtnQuery btnQuery = new BtnQuery();
				btnQuery.setId(btnId);
				btnSet.add((Btn) btnMybatisDao.queryById(btnQuery));
			}
		}
		role.setBtnSet(btnSet);
		
		roleMybatisDao.update(role);
		if(StringUtils.isNotEmpty(roleForm.getBtns())){
			roleMybatisDao.deleteBtnByRole(role);
			roleMybatisDao.addBtnByRole(role);
		}
		json.setSuccess(true);
		return json;
	}

	@CacheEvict(value = "btnCache", allEntries = true)
	@Transactional
	public Json deleteRole(String id) {
		Json json = new Json();
		RoleQuery roleQuery = new RoleQuery();
		roleQuery.setId(id);
		Role role = roleMybatisDao.queryBtnListById(roleQuery);
		if(role != null){
			roleMybatisDao.deleteBtnByRole(role);
			roleMybatisDao.delete(role);
		}
		json.setSuccess(true);
		return json;
	}

	public List<EasyuiCombobox> getRoleCombobox(){
		List<EasyuiCombobox> comboboxList = new ArrayList<EasyuiCombobox>();
		EasyuiCombobox combobox = null;
		List<Role> roleList = roleMybatisDao.queryListByAll();
		if(roleList != null && roleList.size() > 0){
			for(Role role : roleList){
				combobox = new EasyuiCombobox();
				combobox.setId(role.getId());
				combobox.setValue(role.getRoleName());
				comboboxList.add(combobox);
			}
		}
		return comboboxList;
	}
}
