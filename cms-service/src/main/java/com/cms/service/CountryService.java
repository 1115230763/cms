package com.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.cms.dao.CountryDao;
import com.cms.dao.MybatisCriteria;
import com.cms.easyui.EasyuiCombobox;
import com.cms.easyui.EasyuiDatagrid;
import com.cms.easyui.EasyuiDatagridPager;
import com.cms.entity.Country;
import com.cms.query.CountryQuery;
import com.cms.utils.BeanConvertUtil;
import com.cms.vo.CountryVO;
import com.cms.vo.Json;
import com.cms.vo.form.CountryForm;

@Service("countryService")
public class CountryService extends BaseService {
	
	@Autowired
	private CountryDao countryDao;
	
	public EasyuiDatagrid<CountryVO> getCountryDatagrid(EasyuiDatagridPager pager, CountryQuery query) {
		EasyuiDatagrid<CountryVO> datagrid = new EasyuiDatagrid<CountryVO>();
		MybatisCriteria mybatisCriteria = new MybatisCriteria();
		mybatisCriteria.setCurrentPage(pager.getPage());
		mybatisCriteria.setPageSize(pager.getRows());
		mybatisCriteria.setCondition(BeanConvertUtil.bean2Map(query));
		List<Country> countryList = countryDao.queryByPageList(mybatisCriteria);
		
		CountryVO countryVO = null;
		List<CountryVO> countryVOList = new ArrayList<CountryVO>();
		for (Country country : countryList) {
			countryVO = new CountryVO();
			BeanUtils.copyProperties(country, countryVO);
			countryVOList.add(countryVO);
		}
		datagrid.setTotal((long) countryDao.queryByCount(mybatisCriteria));
		datagrid.setRows(countryVOList);
		return datagrid;
	}

	@CacheEvict(value = "countryCache", allEntries = true)
	public Json addCountry(CountryForm countryForm) throws Exception {
		Json json = new Json();
		CountryQuery countryQuery = new CountryQuery();
		countryQuery.setCountryName(countryForm.getCountryName());
		Country country = countryDao.queryById(countryQuery);
		BeanUtils.copyProperties(countryForm, country);
		countryDao.add(country);
		json.setSuccess(true);
		return json;
	}
	
	@CacheEvict(value = "countryCache", allEntries = true)
	public Json editCountry(CountryForm countryForm) {
//		this.testAtomikos();
		Json json = new Json();
		CountryQuery countryQuery = new CountryQuery();
		countryQuery.setCountryName(countryForm.getCountryName());
		Country country = countryDao.queryById(countryQuery);
		BeanUtils.copyProperties(countryForm, country);
		countryDao.update(country);
		json.setSuccess(true);
		
		return json;
	}

	@CacheEvict(value = "countryCache", allEntries = true)
	public Json deleteCountry(int id) {
		Json json = new Json();
		CountryQuery countryQuery = new CountryQuery();
		countryQuery.setCountryName(String.valueOf(id));
		Country country = countryDao.queryById(countryQuery);
		if(country != null){
			countryDao.delete(country);
		}
		json.setSuccess(true);
		return json;
	}
	
	public List<EasyuiCombobox> getCountryCombobox() {
		List<EasyuiCombobox> comboboxList = new ArrayList<EasyuiCombobox>();
		EasyuiCombobox combobox = null;
		List<Country> countryList = countryDao.queryListByAll();
		if(countryList != null && countryList.size() > 0){
			for(Country country : countryList){
				combobox = new EasyuiCombobox();
				combobox.setId(String.valueOf(country.getId()));
				combobox.setValue(country.getCountryName());
				comboboxList.add(combobox);
			}
		}
		return comboboxList;
	}
}
