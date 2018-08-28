package com.cms.dao;
 
import java.util.List;

import com.cms.entity.GoodsOriginType;
 

public interface GoodsOriginDao extends BaseDao{
	List<GoodsOriginType> queryGoodsOriginType(Object obj);
}