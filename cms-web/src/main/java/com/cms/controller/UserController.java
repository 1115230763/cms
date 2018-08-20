package com.cms.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cms.easyui.EasyuiCombobox;
import com.cms.easyui.EasyuiTree;
import com.cms.entity.UserLogin;
import com.cms.service.UserService;
import com.cms.utils.ResourceUtil;
import com.cms.utils.annotation.Login;
import com.cms.utils.editor.CustomDateEditor;
import com.cms.vo.Json;
import com.cms.vo.UserVO;
import com.cms.vo.form.UserForm;

@Controller
@RequestMapping("userController")
public class UserController {
	@Autowired
	private UserService userService;
	
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@Login
	@RequestMapping(params = "toMain")
	public ModelAndView toMain(String menuId) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("menuId", menuId);
		return new ModelAndView("user/main", model);
	}
	
	@Login
	@RequestMapping(params = "getTree")
	@ResponseBody
	public Set<EasyuiTree> showTree() {
		return userService.getUserTree();
	}
	
	@Login
	@RequestMapping(params = "showTreegrid")
	@ResponseBody
	public Set<UserVO> showTreegrid() {
		return userService.getUserTreegrid();
	}
	
	@Login
	@RequestMapping(params = "add")
	@ResponseBody
	public Json add(UserForm userForm) throws Exception {
		return userService.addUser(userForm);
	}
	
	@Login
	@RequestMapping(params = "edit")
	@ResponseBody
	public Json edit(UserForm userForm) {
		return userService.editUser(userForm);
	}
	
	@Login
	@RequestMapping(params = "delete")
	@ResponseBody
	public Json delete(String id) {
		Json json = userService.deleteUser(id);
		if(json == null){
			json = new Json();
		}
		json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		return json;
	}
	
	@Login
	@RequestMapping(params = "getUser")
	@ResponseBody
	public UserVO getUser(HttpSession session) {
		UserLogin userLogin = (UserLogin)session.getAttribute(ResourceUtil.getUserInfo());
		return userService.getUser(userLogin);
	}
	
	@Login
	@RequestMapping(params = "getBtn")
	@ResponseBody
	public Json getBtn(String id, HttpSession session) {
		return userService.getBtn(id, (UserLogin)session.getAttribute(ResourceUtil.getUserInfo()));
	}
	
	@Login
	@RequestMapping(params = "getCustomerCombobox")
	@ResponseBody
	public List<EasyuiCombobox> getCustomerCombobox(HttpSession session) {
		return userService.getCustomerCombobox();
	}
	
	@RequestMapping(params = "getSupervisorCombobox")
	@ResponseBody
	public List<EasyuiCombobox> getSupervisorCombobox() {
		return userService.getSupervisorCombobox();
	}
}
