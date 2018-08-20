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
import com.cms.easyui.EasyuiDatagridPager;
import com.cms.entity.UserLogin;
import com.cms.query.CountryQuery;
import com.cms.service.CountryService;
import com.cms.utils.ResourceUtil;
import com.cms.utils.annotation.Login;
import com.cms.vo.CountryVO;
import com.cms.vo.Json;
import com.cms.vo.form.CountryForm;

@Controller
@RequestMapping("countryController")
public class CountryController {
	@Autowired
	private CountryService countryService;
	
	@Login
	@RequestMapping(params = "toMain")
	public ModelAndView toMain(String menuId) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("menuId", menuId);
		return new ModelAndView("country/main", model);
	}
	
	@Login
	@RequestMapping(params = "showDatagrid")
	@ResponseBody
	public EasyuiDatagrid<CountryVO> showDatagrid(EasyuiDatagridPager pager, CountryQuery query) {
		return countryService.getCountryDatagrid(pager, query);
	}
	
	@Login
	@RequestMapping(params = "add")
	@ResponseBody
	public Json add(CountryForm countryForm) throws Exception {
		Json json = countryService.addCountry(countryForm);
		if(json == null){
			json = new Json();
		}
		json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		return json;
	}
	
	@Login
	@RequestMapping(params = "edit")
	@ResponseBody
	public Json edit(CountryForm countryForm) {
		Json json = countryService.editCountry(countryForm);
		if(json == null){
			json = new Json();
		}
		json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		return json;
	}
	
	@Login
	@RequestMapping(params = "delete")
	@ResponseBody
	public Json delete(int id) {
		Json json = countryService.deleteCountry(id);
		if(json == null){
			json = new Json();
		}
		json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		return json;
	}
	
	@RequestMapping(params = "getCountryCombobox")
	@ResponseBody
	public List<EasyuiCombobox> getCountryCombobox() {
		return countryService.getCountryCombobox();
	}
	
	@Login
	@RequestMapping(params = "getBtn")
	@ResponseBody
	public Json getBtn(String id, HttpSession session) {
		return countryService.getBtn(id, (UserLogin)session.getAttribute(ResourceUtil.getUserInfo()));
	}
}
