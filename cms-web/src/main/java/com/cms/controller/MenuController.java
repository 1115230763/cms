package com.cms.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cms.easyui.EasyuiTree;
import com.cms.entity.UserLogin;
import com.cms.service.MenuService;
import com.cms.utils.ResourceUtil;
import com.cms.utils.annotation.Login;
import com.cms.vo.Json;
import com.cms.vo.MenuVO;
import com.cms.vo.form.MenuForm;

@Controller
@RequestMapping("menuController")
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	@Login
	@RequestMapping(params = "toMain")
	public ModelAndView toMain(String menuId) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("menuId", menuId);
		return new ModelAndView("menu/main", model);
	}
	
	@Login
	@RequestMapping(params = "showTree")
	@ResponseBody
	public Set<EasyuiTree> showTree(HttpSession session) {
		UserLogin userLogin = (UserLogin)session.getAttribute(ResourceUtil.getUserInfo());
		return menuService.showMenuTree(userLogin);
	}
	
	@Login
	@RequestMapping(params = "showTreegrid")
	@ResponseBody
	public Set<MenuVO> showTreegrid(HttpSession session) {
		return menuService.showMenuTreegrid(session);
	}
	
	@Login
	@RequestMapping(params = "add")
	@ResponseBody
	public Json add(MenuForm processMenuVO, HttpSession session) {
		UserLogin userLogin = (UserLogin)session.getAttribute(ResourceUtil.getUserInfo());
		Json json = menuService.addMenu(processMenuVO, userLogin);
		if(json == null){
			json = new Json();
		}
		json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		return json;
	}
	
	@Login
	@RequestMapping(params = "edit")
	@ResponseBody
	public Json edit(MenuForm processMenuVO) {
		Json json = menuService.editMenu(processMenuVO);
		if(json == null){
			json = new Json();
		}
		json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		return json;
	}
	
	@Login
	@RequestMapping(params = "delete")
	@ResponseBody
	public Json delete(String id) {
		Json json = menuService.deleteMenu(id);
		if(json == null){
			json = new Json();
		}
		json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		return json;
	}
	
	@Login
	@RequestMapping(params = "getBtn")
	@ResponseBody
	public Json getBtn(String id, HttpSession session) {
		return menuService.getBtn(id, (UserLogin)session.getAttribute(ResourceUtil.getUserInfo()));
	}
}
