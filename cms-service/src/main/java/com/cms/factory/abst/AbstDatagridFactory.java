package com.cms.factory.abst;

import com.cms.easyui.EasyuiDatagrid;
import com.cms.easyui.EasyuiDatagridPager;
import com.cms.product.abst.AbstDatagrid;
import com.cms.query.IQuery;
import com.cms.vo.iface.IDatagridVO;

public abstract class AbstDatagridFactory {
	public enum CreateDatagridTypeEnum {
		SYS_BTN_DATAGRID,
		SYS_ROLE_DATAGRID;
	}
	
	public EasyuiDatagrid<IDatagridVO> createDatagrid(CreateDatagridTypeEnum createDatagridType, EasyuiDatagridPager pager, IQuery query) throws Exception {
		return chooseCreateDatagrid(createDatagridType).createDatagrid(pager, query);
    }
	
	public EasyuiDatagrid<IDatagridVO> createDatagrid(CreateDatagridTypeEnum createDatagridType, IQuery query) throws Exception {
		return chooseCreateDatagrid(createDatagridType).createDatagrid(query);
    }
	
	public EasyuiDatagrid<IDatagridVO> createDatagrid(CreateDatagridTypeEnum createDatagridType) throws Exception {
		return chooseCreateDatagrid(createDatagridType).createDatagrid();
    }
	
	protected abstract AbstDatagrid chooseCreateDatagrid(CreateDatagridTypeEnum createDatagridType);
}
