package com.cms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cms.dao.MenuDao;
import com.cms.dao.RoleDao;
import com.cms.dao.UserDao;
import com.cms.easyui.EasyuiTree;
import com.cms.entity.Menu;
import com.cms.entity.Role;
import com.cms.entity.User;
import com.cms.entity.UserLogin;
import com.cms.query.MenuQuery;
import com.cms.query.RoleQuery;
import com.cms.utils.ResourceUtil;
import com.cms.utils.comparator.MenuComparator;
import com.cms.vo.Json;
import com.cms.vo.MenuVO;
import com.cms.vo.form.MenuForm;

@Service("menuService")
public class MenuService extends BaseService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private RoleDao roleDao;
	
	/**
	 * 取得左列Menu Tree
	 * @param session
	 * @return
	 */
	@Cacheable(value = "menuCache", key = "#userLogin.id")
	public Set<EasyuiTree> showMenuTree(UserLogin userLogin) {
		Set<EasyuiTree> easyuiTreeSet = new TreeSet<EasyuiTree>();
		if(userLogin != null){
			User user = userDao.queryListById(userLogin);
			List<Menu> allMenuList = new ArrayList<Menu>();
			for(Role role : user.getRoleSet()){;
				for(Menu menu : role.getMenuSet()){
					allMenuList.add(menu);
				}
			}
			
			if(allMenuList != null && allMenuList.size() > 0){
				Collections.sort(allMenuList, new MenuComparator());// 排序
				for(Menu menu : allMenuList){
					if(StringUtils.isEmpty(menu.getParentId())){
						easyuiTreeSet.add(this.getMenuTreeNode(menu, allMenuList));
					}
				}
			}
		}
		return easyuiTreeSet;
	}

	/**
	 * 取得Menu Tree所有子节点
	 * @param menu
	 * @param allMenuList 
	 * @return
	 */
	private EasyuiTree getMenuTreeNode(Menu menu, List<Menu> allMenuList) {
		EasyuiTree tree = new EasyuiTree();
		tree.setId(menu.getId());
		tree.setText(menu.getMenuName());
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("menuId", menu.getId());
		attributes.put("src", menu.getUrl());
		attributes.put("type", menu.getMenuType());
		attributes.put("seq", menu.getDisplaySeq());
		tree.setAttributes(attributes);
		
		List<Menu> subMenuList = new ArrayList<Menu>();
		for(Menu subMenu : allMenuList){
			if(subMenu.getParentId()!=null){
				if(subMenu.getParentId().equals(menu.getId())){
					subMenuList.add(subMenu);
				}
			}
		}
		
		if(subMenuList != null && subMenuList.size() > 0){
			Collections.sort(subMenuList, new MenuComparator());// 排序
			
			Set<EasyuiTree> subTreeSet = new TreeSet<EasyuiTree>();
			for (Menu subMenu : subMenuList) {
				subTreeSet.add(this.getMenuTreeNode(subMenu, allMenuList));
			}
			
			if(subTreeSet.size() > 0){
				tree.setState("closed");
				tree.setChildren(subTreeSet);
			}
		}
		return tree;
	}

	/**
	 * 取得管理表单所有资料
	 * @param session
	 * @return
	 */
	public Set<MenuVO> showMenuTreegrid(HttpSession session) {
		UserLogin userLogin = (UserLogin)session.getAttribute(ResourceUtil.getUserInfo());
		Set<MenuVO> menuVoSet = new TreeSet<MenuVO>();
		if(userLogin != null){
			User user = userDao.queryListById(userLogin);
			List<Menu> allMenuList = new ArrayList<Menu>();
			for(Role role : user.getRoleSet()){
				for(Menu menu : role.getMenuSet()){
					allMenuList.add(menu);
				}
			}
			
			Collections.sort(allMenuList, new MenuComparator());// 排序
			for(Menu menu : allMenuList){
				if(StringUtils.isEmpty(menu.getParentId())){
					menuVoSet.add(this.getMenuTreegridNode(menu, allMenuList));
				}
			}
		}
		return menuVoSet;
	}

	/**
	 * 取得Menu TreeGrid所有子节点
	 * @param menu
	 * @param allMenuList
	 * @return
	 */
	private MenuVO getMenuTreegridNode(Menu menu, List<Menu> allMenuList) {
		MenuVO menuVO = new MenuVO();
		MenuQuery menuQuery = new MenuQuery();
		menuQuery.setId(menu.getId());
		menu = menuDao.queryRoleListById(menuQuery);
		BeanUtils.copyProperties(menu, menuVO);
		menuVO.setRoleSet(menu.getRoleSet());
		
		List<Menu> subMenuList = new ArrayList<Menu>();
		for(Menu subMenu : allMenuList){
			if(subMenu.getParentId()!=null){
				if(subMenu.getParentId().equals(menu.getId())){
					subMenuList.add(subMenu);
				}
			}
		}
		
		if(subMenuList != null && subMenuList.size() > 0){
			Collections.sort(subMenuList, new MenuComparator());// 排序
			
			MenuVO subMenuVo = null;
			Map<String, String> parent = null;
			Set<MenuVO> childrenSet = new TreeSet<MenuVO>();
			for (Menu subMenu : subMenuList) {
				subMenuVo = this.getMenuTreegridNode(subMenu, allMenuList);
				parent = new HashMap<String, String>();
				parent.put("menuId", menu.getId());
				parent.put("menuName", menu.getMenuName());
				subMenuVo.setParent(parent);
				childrenSet.add(subMenuVo);
			}
			
			if(childrenSet.size() > 0){
				menuVO.setState("closed");
				menuVO.setChildren(childrenSet);
			}
		}
		return menuVO;
	}
	
	/**
	 * 新增功能列表资料
	 * @param menuForm
	 * @param userLogin
	 * @return
	 */
	@CacheEvict(value = "menuCache",  key = "#userLogin.id")
	@Transactional
	public Json addMenu(MenuForm menuForm, UserLogin userLogin) {
		Json json = new Json();
		
		Menu menu = new Menu();
		BeanUtils.copyProperties(menuForm, menu);
		
		RoleQuery roleQuery = new RoleQuery();
		
		Set<Role> roleSet = new HashSet<Role>();
		roleQuery.setId("1");
		Role role = roleDao.queryMenuListById(roleQuery);
		roleSet.add(role);
		String[] roleIdArray = menuForm.getRoleList().split(",");
		for(String roleId : roleIdArray){
			roleQuery.setId(roleId);
			role = roleDao.queryMenuListById(roleQuery);
			roleSet.add(role);
		}
		
		menu.setRoleSet(roleSet);
		menuDao.add(menu);
		menuDao.deleteRoleByMenu(menu);
		menuDao.addRoleByMenu(menu);
		json.setSuccess(true);
		return json;
	}
	
	/**
	 * 修改功能列表资料
	 * @param menuForm
	 * @return
	 */
	@CacheEvict(value = "menuCache", allEntries = true)
	@Transactional
	public Json editMenu(MenuForm menuForm) {
		Json json = new Json();

		MenuQuery menuQuery = new MenuQuery();
		RoleQuery roleQuery = new RoleQuery();
		menuQuery.setId(menuForm.getMenuId());
		Menu menu = menuDao.queryById(menuQuery);
		String oldParentId = menu.getParentId();
		BeanUtils.copyProperties(menuForm, menu);
		
		if(menu.getId().equals(menu.getParentId())){
			menu.setParentId(oldParentId);
		}
		
		Set<Role> roleSet = new HashSet<Role>();
		roleQuery.setId("1");
		Role role = roleDao.queryMenuListById(roleQuery);
		roleSet.add(role);
		String[] roleIdArray = menuForm.getRoleList().split(",");
		for(String roleId : roleIdArray){
			roleQuery.setId(roleId);
			role = roleDao.queryMenuListById(roleQuery);
			roleSet.add(role);
		}
		
		menu.setRoleSet(roleSet);
		menuDao.update(menu);
		menuDao.deleteRoleByMenu(menu);
		menuDao.addRoleByMenu(menu);
		
		json.setSuccess(true);
		return json;
	}

	/**
	 * 刪除功能列表资料
	 * @param id
	 * @return
	 */
	@CacheEvict(value = "menuCache", allEntries = true)
	public Json deleteMenu(String id) {
		Json json = new Json();
		MenuQuery menuQuery = new MenuQuery();
		menuQuery.setId(id);
		Menu menu = menuDao.queryById(menuQuery);
		recursiveDelete(menu);
		json.setSuccess(true);
		return json;
	}
	
	/**
	 * 递归刪除功能列表
	 * @param menu
	 */
	private void recursiveDelete(Menu menu){
		if(menu != null){
			MenuQuery menuQuery = new MenuQuery();
			menuQuery.setId(menu.getId());
			List<Menu> subMenuList = menuDao.queryByParentId(menuQuery);
			if(subMenuList != null && subMenuList.size() > 0){
				for(Menu subMenu : subMenuList){
					recursiveDelete(subMenu);
				}
			}
			menuDao.deleteRoleByMenu(menu);
			menuDao.delete(menu);
		}
	}
}
