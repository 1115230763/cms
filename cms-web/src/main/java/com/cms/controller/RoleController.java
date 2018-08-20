package com.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cms.easyui.EasyuiCombobox;
import com.cms.easyui.EasyuiDatagrid;
import com.cms.entity.UserLogin;
import com.cms.service.RoleService;
import com.cms.utils.ResourceUtil;
import com.cms.utils.annotation.Login;
import com.cms.vo.Json;
import com.cms.vo.RoleVO;
import com.cms.vo.form.RoleForm;

@Controller
@RequestMapping("roleController")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@Login
	@RequestMapping(params = "toMain")
	public ModelAndView toMain(String menuId) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("menuId", menuId);
		return new ModelAndView("role/main", model);
	}
	
	@Login
	@RequestMapping(params = "showDatagrid")
	@ResponseBody
	public EasyuiDatagrid<RoleVO> showDatagrid() {
		return roleService.getRoleDatagrid();
	}
	
	@Login
	@RequestMapping(params = "add")
	@ResponseBody
	public Json add(RoleForm roleForm) {
		Json json = roleService.addRole(roleForm);
		if(json == null){
			json = new Json();
		}
		json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		return json;
	}
	
	@Login
	@RequestMapping(params = "edit")
	@ResponseBody
	public Json edit(RoleForm roleForm) {
		Json json = roleService.editRole(roleForm);
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
		Json json = roleService.deleteRole(id);
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
		return roleService.getBtn(id, (UserLogin)session.getAttribute(ResourceUtil.getUserInfo()));
	}
	
	@Login
	@RequestMapping(params = "getCombobox")
	@ResponseBody
	public List<EasyuiCombobox> getCombobox(HttpSession session) {
		return roleService.getRoleCombobox();
	}
}
