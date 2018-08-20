package com.cms.product.abst;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.cms.easyui.EasyuiDatagrid;
import com.cms.easyui.EasyuiDatagridPager;
import com.cms.query.IQuery;
import com.cms.vo.iface.IDatagridVO;

public abstract class AbstDatagrid extends SpringBeanAutowiringSupport {

	public abstract EasyuiDatagrid<IDatagridVO> createDatagrid(EasyuiDatagridPager pager, IQuery query);
	
	public abstract EasyuiDatagrid<IDatagridVO> createDatagrid(IQuery query);
	
	public abstract EasyuiDatagrid<IDatagridVO> createDatagrid();

}
