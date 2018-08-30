package com.cms.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cms.entity.UserLogin;
import com.cms.service.TestService;
import com.cms.utils.UserLoginUtil;
import com.cms.utils.annotation.Login;

 
@Controller
@RequestMapping("testController")
public class TestController {

	@Autowired
	private TestService testService;
	
	@Login
	@RequestMapping(params = "toMain")
	public ModelAndView toMain(String menuId) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("menuId", menuId);
		return new ModelAndView("goodsOrigin/menu", model);
	}
	@RequestMapping(params = "getTree")
	@ResponseBody
	public List<Map<String, Object>> getUserTree() {
		UserLogin userLogin=UserLoginUtil.getLoginUser();
		return testService.showMenuTree(userLogin);
	}
	
 
}
