package com.cms.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
	@RequestMapping("/index.html")
	public ModelAndView login() throws Exception{
		return new ModelAndView("layout/login");
	}
	
	@RequestMapping("/main.html")
	public ModelAndView toHome() throws Exception{
		return new ModelAndView("layout/main");
	}
	
	@RequestMapping("/head.html")
	public ModelAndView head() throws Exception{
		return new ModelAndView("layout/header");
	}
	
	@RequestMapping("/foot.html")
	public ModelAndView foot() throws Exception{
		return new ModelAndView("layout/footer");
	}
	
	@RequestMapping("/menu.html")
	public ModelAndView menu() throws Exception{
		return new ModelAndView("layout/menu");
	}
	
	@RequestMapping("/home.html")
	public ModelAndView home() throws Exception{
		return new ModelAndView("home/main");
	}
	
	@RequestMapping("/reLogin.html")
	public ModelAndView reLogin() throws Exception{
		return new ModelAndView("home/reLogin");
	}
	
	@RequestMapping("/logout.html")
	@ResponseBody
	public boolean logout(HttpSession session) throws Exception{
		if(session != null){
			session.invalidate();
		}
		return true;
	}
}
