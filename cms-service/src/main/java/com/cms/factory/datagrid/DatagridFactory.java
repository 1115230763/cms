package com.cms.factory.datagrid;

import com.cms.factory.abst.AbstDatagridFactory;
import com.cms.product.abst.AbstDatagrid;
import com.cms.product.datagrid.SysBtnDatagrid;
import com.cms.product.datagrid.SysRoleDatagrid;

public class DatagridFactory extends AbstDatagridFactory {

	protected AbstDatagrid chooseCreateDatagrid(CreateDatagridTypeEnum createDatagridType) {
		AbstDatagrid datagrid = null;
		switch (createDatagridType) {
			case SYS_BTN_DATAGRID : 							datagrid = new SysBtnDatagrid(); 						break;
			case SYS_ROLE_DATAGRID :							datagrid = new SysRoleDatagrid(); 						break;
			default: break;
		}
		return datagrid;
	}

}
