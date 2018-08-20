package com.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.dao.BtnDao;
import com.cms.dao.MybatisCriteria;
import com.cms.easyui.EasyuiCombobox;
import com.cms.easyui.EasyuiDatagrid;
import com.cms.easyui.EasyuiDatagridPager;
import com.cms.entity.Btn;
import com.cms.query.BtnQuery;
import com.cms.utils.BeanConvertUtil;
import com.cms.vo.BtnVO;
import com.cms.vo.Json;
import com.cms.vo.form.BtnForm;

@Service("btnService")
public class BtnService extends BaseService {

	@Autowired
	private BtnDao btnDao;

	public EasyuiDatagrid<BtnVO> getPagedDatagrid(EasyuiDatagridPager pager, BtnQuery query) {
		EasyuiDatagrid<BtnVO> datagrid = new EasyuiDatagrid<BtnVO>();
		MybatisCriteria mybatisCriteria = new MybatisCriteria();
		mybatisCriteria.setCurrentPage(pager.getPage());
		mybatisCriteria.setPageSize(pager.getRows());
		mybatisCriteria.setCondition(BeanConvertUtil.bean2Map(query));
		List<Btn> btnList = btnDao.queryByPageList(mybatisCriteria);
		BtnVO btnVO = null;
		List<BtnVO> btnVOList = new ArrayList<BtnVO>();
		for (Btn btn : btnList) {
			btnVO = new BtnVO();
			BeanUtils.copyProperties(btn, btnVO);
			btnVOList.add(btnVO);
		}
		datagrid.setTotal((long) btnDao.queryByCount(mybatisCriteria));
		datagrid.setRows(btnVOList);
		return datagrid;
	}

	public Json addBtn(BtnForm btnForm) throws Exception {
		Json json = new Json();
		Btn btn = new Btn();
		BeanUtils.copyProperties(btnForm, btn);
		btnDao.add(btn);
		json.setSuccess(true);
		return json;
	}

	public Json editBtn(BtnForm btnForm) {
		Json json = new Json();
		BtnQuery btnQuery = new BtnQuery();
		btnQuery.setId(btnForm.getBtnId());
		Btn btn = btnDao.queryById(btnQuery);
		BeanUtils.copyProperties(btnForm, btn);
		btnDao.update(btn);
		json.setSuccess(true);
		return json;
	}

	public Json deleteBtn(String id) {
		Json json = new Json();
		BtnQuery btnQuery = new BtnQuery();
		btnQuery.setId(id);
		Btn btn = btnDao.queryById(btnQuery);
		if(btn != null){
			btnDao.delete(btn);
		}
		json.setSuccess(true);
		return json;
	}

	public List<EasyuiCombobox> getBtnCombobox() {
		List<EasyuiCombobox> comboboxList = new ArrayList<EasyuiCombobox>();
		EasyuiCombobox combobox = null;
		List<Btn> btnList = btnDao.queryListByAll();
		if(btnList != null && btnList.size() > 0){
			for(Btn btn : btnList){
				combobox = new EasyuiCombobox();
				combobox.setId(String.valueOf(btn.getId()));
				combobox.setValue(btn.getBtnChsName());
				comboboxList.add(combobox);
			}
		}
		return comboboxList;
	}

	public Json findAll() {
		Json json = new Json();
		List<Btn> btlList = btnDao.queryListByAll();
		StringBuilder sb = new StringBuilder();
		for(Btn btn : btlList){
			sb.append(btn.getBtnName()).append(",");
		}
		if(sb.length() > 0){
			sb.deleteCharAt(sb.lastIndexOf(","));
			json.setSuccess(true);
			json.setObj(sb.toString());sb.setLength(0);
		}
		return json;
	}
}