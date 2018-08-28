package com.cms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cms.entity.GoodsOrigin;
import com.cms.entity.GoodsOriginType;
import com.cms.dao.MybatisCriteria;
import com.cms.dao.GoodsOriginDao;
import com.cms.utils.BeanConvertUtil;
import com.cms.utils.QiniuUtils;
import com.cms.utils.ResourceUtil;
import com.cms.utils.UserLoginUtil;
import com.cms.vo.GoodsOriginVO;
import com.cms.vo.Json;
import com.cms.easyui.EasyuiCombobox;
import com.cms.easyui.EasyuiDatagrid;
import com.cms.easyui.EasyuiDatagridPager;
import com.cms.vo.form.GoodsOriginForm;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.cms.query.GoodsOriginQuery;

@Service("goodsOriginService")
public class GoodsOriginService extends BaseService {

	@Autowired
	private GoodsOriginDao goodsOriginDao;

	public EasyuiDatagrid<GoodsOriginVO> getPagedDatagrid(EasyuiDatagridPager pager, GoodsOriginQuery query) {
		EasyuiDatagrid<GoodsOriginVO> datagrid = new EasyuiDatagrid<GoodsOriginVO>();
		MybatisCriteria mybatisCriteria = new MybatisCriteria();
		mybatisCriteria.setCurrentPage(pager.getPage());
		mybatisCriteria.setPageSize(pager.getRows());
		mybatisCriteria.setCondition(BeanConvertUtil.bean2Map(query));
		List<GoodsOrigin> goodsOriginList = goodsOriginDao.queryByPageList(mybatisCriteria);
		GoodsOriginVO goodsOriginVO = null;
		List<GoodsOriginVO> goodsOriginVOList = new ArrayList<GoodsOriginVO>();
		for (GoodsOrigin goodsOrigin : goodsOriginList) {
			goodsOriginVO = new GoodsOriginVO();
			BeanUtils.copyProperties(goodsOrigin, goodsOriginVO);
			goodsOriginVOList.add(goodsOriginVO);
		}
		datagrid.setTotal((long) goodsOriginDao.queryByCount(mybatisCriteria));
		datagrid.setRows(goodsOriginVOList);
		return datagrid;
	}

	public Json addGoodsOrigin(GoodsOriginForm goodsOriginForm) throws Exception {
		Json json = new Json();
		GoodsOrigin goodsOrigin = new GoodsOrigin();
		BeanUtils.copyProperties(goodsOriginForm, goodsOrigin);
		goodsOriginDao.add(goodsOrigin);
		json.setSuccess(true);
		return json;
	}

	public Json editGoodsOrigin(MultipartFile goodsImageFile,GoodsOriginForm goodsOriginForm) {
		Json json = new Json();
		GoodsOriginQuery goodsOriginQuery = new GoodsOriginQuery();
		goodsOriginQuery.setGoodsId(goodsOriginForm.getGoodsId());
		if(goodsOriginForm.getExpiryDate()!=null){
		if(goodsOriginForm.getExpiryDateUnit().equals("天")){
			goodsOriginForm.setExpiryDate(goodsOriginForm.getExpiryDate());
		}else if(goodsOriginForm.getExpiryDateUnit().equals("月")){
			goodsOriginForm.setExpiryDate(goodsOriginForm.getExpiryDate()*30);
		}else if(goodsOriginForm.getExpiryDateUnit().equals("年")){
			goodsOriginForm.setExpiryDate(goodsOriginForm.getExpiryDate()*365);
		}}else{
			goodsOriginForm.setExpiryDate(null);
		}
		String fileurl;
		String fileName=goodsImageFile.getOriginalFilename();
		try {
			//上传文件
			int extStart = fileName.lastIndexOf('.');
			if(extStart!=-1){
				String ext = fileName.substring(extStart, fileName.length()).toUpperCase();
				fileurl=QiniuUtils.upload(goodsImageFile,ext);
				goodsOriginForm.setGoodsImage(fileurl);
			}
		} catch (Exception e) {
			json.setSuccess(true);
			json.setMsg("图片上传失败！");
			return json;
		} 
		goodsOriginForm.setUpdateBy(UserLoginUtil.getLoginUser().getId());
		goodsOriginForm.setUpdateDate(new Date());
		GoodsOrigin goodsOrigin = goodsOriginDao.queryById(goodsOriginQuery);
		//删除原本文件
//		try {
//			if(goodsOrigin.getGoodsImage()!=null && !goodsOrigin.getGoodsImage().equals("")){
//				String key=goodsOrigin.getGoodsImage().replace(ResourceUtil.getString("domain"), "");
//				QiniuUtils.deleteBykey(key);
//			}
//		} catch (QiniuException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		BeanUtils.copyProperties(goodsOriginForm, goodsOrigin);
		if(goodsOrigin.getGoodsImage()==null){
			goodsOrigin.setGoodsImage("");
		}
		goodsOriginDao.update(goodsOrigin);
		json.setSuccess(true);
		json.setMsg("修改成功");
		return json;
	}

	public Json deleteGoodsOrigin(String id) {
		Json json = new Json();
		GoodsOriginQuery goodsOriginQuery = new GoodsOriginQuery();
		GoodsOrigin goodsOrigin = goodsOriginDao.queryById(goodsOriginQuery);
		if(goodsOrigin != null){
			goodsOriginDao.delete(goodsOrigin);
		}
		json.setSuccess(true);
		return json;
	}

	public List<EasyuiCombobox> getGoodsOriginCombobox() {
		List<EasyuiCombobox> comboboxList = new ArrayList<EasyuiCombobox>();
		EasyuiCombobox combobox = null;
		List<GoodsOrigin> goodsOriginList = goodsOriginDao.queryByAll();
		if(goodsOriginList != null && goodsOriginList.size() > 0){
			for(GoodsOrigin goodsOrigin : goodsOriginList){
				combobox = new EasyuiCombobox();
				combobox.setId(String.valueOf(goodsOrigin.getGoodsId()));
				combobox.setValue(goodsOrigin.getGoodsName());
				comboboxList.add(combobox);
			}
		}
		return comboboxList;
	}
	public List<EasyuiCombobox> getGoodsOriginTypeCombobox() {
		List<EasyuiCombobox> comboboxList = new ArrayList<EasyuiCombobox>();
		EasyuiCombobox combobox = null;
		List<GoodsOriginType> goodsOriginList = goodsOriginDao.queryGoodsOriginType(new Object());
		if(goodsOriginList != null && goodsOriginList.size() > 0){
			for(GoodsOriginType goodsOrigin : goodsOriginList){
				combobox = new EasyuiCombobox();
				combobox.setId(String.valueOf(goodsOrigin.getTypeId()));
				combobox.setValue(goodsOrigin.getName());
				comboboxList.add(combobox);
			}
		}
		return comboboxList;
	}

	public Json uploadImg(MultipartFile file,String ext, HttpServletRequest request){
		Json json=new Json(); 
		String fileurl;
		try {
			//上传文件
			fileurl=QiniuUtils.upload(file,ext);
			//获取key 也就是文件名
			String key=fileurl.replace(ResourceUtil.getString("domain"), "");
			//设置有效期1天
			QiniuUtils.deleteAfterDays(key);
		} catch (Exception e) {
			json.setMsg("上传图片异常！");
			json.setSuccess(false);
			return json;
		}
		json.setSuccess(true);
		json.setObj(fileurl);
		return json;
	}

	 
 
 
}