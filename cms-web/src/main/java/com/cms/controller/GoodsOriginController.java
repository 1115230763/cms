package com.cms.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.cms.entity.UserLogin;
import com.cms.service.GoodsOriginService;
import com.cms.utils.ResourceUtil;
import com.cms.utils.annotation.Login;
import com.cms.vo.Json;
import com.cms.vo.GoodsOriginVO; 
import com.cms.easyui.EasyuiCombobox;
import com.cms.easyui.EasyuiDatagrid;
import com.cms.easyui.EasyuiDatagridPager;
import com.cms.vo.form.GoodsOriginForm;
import com.cms.query.GoodsOriginQuery; 

@Controller
@RequestMapping("goodsOriginController")
public class GoodsOriginController {

	@Autowired
	private GoodsOriginService goodsOriginService;

	@Login
	@RequestMapping(params = "toMain")
	public ModelAndView toMain(String menuId) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("menuId", menuId);
		return new ModelAndView("goodsOrigin/bootstrapTest", model);
	}
	@Login
	@RequestMapping(params = "bootstrapTestInfo")
	public ModelAndView bootstrapTestInfo(String menuId) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("menuId", menuId);
		return new ModelAndView("goodsOrigin/bootstrapTestInfo", model);
	} 
	
	@Login
	@RequestMapping(params = "showDatagrid")
	@ResponseBody
	public EasyuiDatagrid<GoodsOriginVO> showDatagrid(EasyuiDatagridPager pager, GoodsOriginQuery query) {
		return goodsOriginService.getPagedDatagrid(pager, query);
	}

	@Login
	@RequestMapping(params = "add")
	@ResponseBody
	public Json add(GoodsOriginForm goodsOriginForm) throws Exception {
		Json json = goodsOriginService.addGoodsOrigin(goodsOriginForm);
		if(json == null){
			json = new Json();
			json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		}
		return json;
	}

	@Login
	@RequestMapping(params = "edit")
	@ResponseBody
	public Json edit(MultipartFile goodsImageFile,GoodsOriginForm goodsOriginForm) throws Exception {
		Json json = goodsOriginService.editGoodsOrigin(goodsImageFile,goodsOriginForm);
		if(json == null){
			json = new Json();
			json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		}
		return json;
	}

	@Login
	@RequestMapping(params = "delete")
	@ResponseBody
	public Json delete(String id) {
		Json json = goodsOriginService.deleteGoodsOrigin(id);
		if(json == null){
			json = new Json();
			json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		}
		return json;
	}

	@Login
	@RequestMapping(params = "getBtn")
	@ResponseBody
	public Json getBtn(String id, HttpSession session) {
		return goodsOriginService.getBtn(id, (UserLogin)session.getAttribute(ResourceUtil.getUserInfo()));
	}

	@Login
	@RequestMapping(params = "getCombobox")
	@ResponseBody
	public List<EasyuiCombobox> getCombobox() {
		return goodsOriginService.getGoodsOriginCombobox();
	}
	@Login
	@RequestMapping(params = "getGoodsOriginTypeCombobox")
	@ResponseBody
	public List<EasyuiCombobox> getGoodsOriginTypeCombobox() {
		return goodsOriginService.getGoodsOriginTypeCombobox();
	} 
	@Login
	@ResponseBody
	@RequestMapping(params = "upload")
	public Json upload(MultipartFile file,String ext,HttpServletRequest request)  { 
		return goodsOriginService.uploadImg(file,ext,request); 
	} 

}