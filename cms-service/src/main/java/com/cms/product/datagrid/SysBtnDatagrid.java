package com.cms.product.datagrid;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cms.dao.BtnDao;
import com.cms.dao.MybatisCriteria;
import com.cms.easyui.EasyuiDatagrid;
import com.cms.easyui.EasyuiDatagridPager;
import com.cms.entity.Btn;
import com.cms.product.abst.AbstDatagrid;
import com.cms.query.BtnQuery;
import com.cms.query.IQuery;
import com.cms.utils.BeanConvertUtil;
import com.cms.utils.BeanUtils;
import com.cms.vo.BtnVO;
import com.cms.vo.iface.IDatagridVO;

/**
 * 需权限控制按钮列表页
 * @author BML_OwenHuang
 *
 */
public class SysBtnDatagrid extends AbstDatagrid {

	@Autowired
	private BtnDao btnDao;
	
	@Override
	public EasyuiDatagrid<IDatagridVO> createDatagrid(EasyuiDatagridPager pager, IQuery query) {
		BtnVO btnVO = null;
		
		List<IDatagridVO> btnVOList = new ArrayList<>();
		EasyuiDatagrid<IDatagridVO> datagrid = new EasyuiDatagrid<>();

		MybatisCriteria mybatisCriteria = new MybatisCriteria();
		mybatisCriteria.setCurrentPage(pager.getPage());
		mybatisCriteria.setPageSize(pager.getRows());
		mybatisCriteria.setCondition(BeanConvertUtil.bean2Map((BtnQuery)query));
		List<Btn> btnList = btnDao.queryByList(mybatisCriteria);
		for (Btn btn : btnList) {
			btnVO = new BtnVO();
			BeanUtils.copyProperties(btn, btnVO);
			btnVOList.add(btnVO);
		}
		datagrid.setTotal((long) btnDao.queryByCount(mybatisCriteria));
		datagrid.setRows(btnVOList);
		return datagrid;
	}

	@Override
	public EasyuiDatagrid<IDatagridVO> createDatagrid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EasyuiDatagrid<IDatagridVO> createDatagrid(IQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

}
