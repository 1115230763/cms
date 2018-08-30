package com.cms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cms.dao.UserDao;
import com.cms.entity.Menu;
import com.cms.entity.Role;
import com.cms.entity.User;
import com.cms.entity.UserLogin;
import com.cms.utils.LoginUtil;
import com.cms.utils.comparator.MenuComparator;
@Service
public class TestService {
	@Autowired
	private UserDao userDao;
	
	/**
	 * 取得左列Menu Tree
	 * @param session
	 * @return
	 */
	public List<Map<String, Object>> showMenuTree(UserLogin userLogin) {
		List<Map<String, Object>> returnmap=new ArrayList<Map<String, Object>>();
		if(userLogin != null){
			User user = userDao.queryListById(userLogin);
			List<Menu> allMenuList = new ArrayList<Menu>();
			for(Role role : user.getRoleSet()){;
				for(Menu menu : role.getMenuSet()){
					allMenuList.add(menu);
				}
			}
			Collections.sort(allMenuList, new MenuComparator());// 排序
			returnmap=this.getTreeList(null,null,allMenuList);
		}
		return returnmap;
	}
	
	
	/**
     * 获取树
     * @方法名:getTreeList 
     * @参数 @param kpi
     * @参数 @return  
     * @返回类型 String
     */
    public List<Map<String, Object>> getTreeList(Menu kpi,List<Map<String, Object>> listmap,List<Menu> list) {
        
        if(listmap == null) {
            listmap = new ArrayList<Map<String,Object>>();
            for(Menu k: list) {
                if(k.getParentId() == null || "".equals(k.getParentId()) || "null".equals(k.getParentId())) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", k.getId().toString());
                    map.put("text", k.getMenuName());
                    map.put("href", k.getUrl());
                    map.put("loginUserId", LoginUtil.getLoginUser().getId());
                    map.put("loginUserName", LoginUtil.getLoginUser().getUserName());
                    listmap.add(map);
                }
            }
            getTreeList(kpi,listmap,list);
            
        } else if(listmap.size()>0 && list.size()>0) {
            for(Map<String, Object> mp:listmap) {
                List<Map<String, Object>> childlist = new ArrayList<Map<String,Object>>();
                for(Menu k:list) {
                    String id = mp.get("id")+"";
                    String pid = k.getParentId()+"";
                    if(id.equals(pid)) {
                        Map<String, Object> m = new HashMap<String, Object>();
                        m.put("id", k.getId().toString());
                        m.put("text", k.getMenuName());
                        m.put("href", k.getUrl());
                        childlist.add(m);
                    }
                }
                if(childlist.size()>0) {
                    List<String> sizelist = new ArrayList<String>();
                    sizelist.add(childlist.size()+"");
                    mp.put("nodes", childlist);
                    mp.put("tags", sizelist);
                    getTreeList(kpi,childlist,list);
                }
            }
        }
        return listmap;
    }
}
