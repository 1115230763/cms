package com.cms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cms.dao.CountryDao;
import com.cms.dao.MybatisCriteria;
import com.cms.dao.RoleDao;
import com.cms.dao.UserDao;
import com.cms.dao.UserLoginDao;
import com.cms.easyui.EasyuiCombobox;
import com.cms.easyui.EasyuiDatagrid;
import com.cms.easyui.EasyuiDatagridPager;
import com.cms.easyui.EasyuiTree;
import com.cms.entity.Country;
import com.cms.entity.Customer;
import com.cms.entity.Role;
import com.cms.entity.User;
import com.cms.entity.UserLogin;
import com.cms.query.CustomerQuery;
import com.cms.query.RoleQuery;
import com.cms.query.UserLoginQuery;
import com.cms.query.UserQuery;
import com.cms.utils.BeanConvertUtil;
import com.cms.utils.EncryptUtil;
import com.cms.utils.MailUtil;
import com.cms.utils.RandomUtil;
import com.cms.utils.ResourceUtil;
import com.cms.utils.SendSMSUtil;
import com.cms.utils.SmsUtil;
import com.cms.utils.UserLoginUtil;
import com.cms.utils.comparator.UserComparator;
import com.cms.utils.vo.MailVO;
import com.cms.vo.Json;
import com.cms.vo.UserVO;
import com.cms.vo.form.UserForm;

@Service("userService")
public class UserService extends BaseService {
	@Autowired
	private UserDao userMybatisDao;
	@Autowired
	private RoleDao roleMybatisDao;
	@Autowired
	private CountryDao countryMybatisDao;
	@Autowired
	private UserLoginDao userLoginMybatisDao;
	@Autowired
	private JavaMailSender mailSender;
	
	public EasyuiDatagrid<UserVO> getPagedDatagrid(EasyuiDatagridPager pager, UserQuery query) {
		EasyuiDatagrid<UserVO> datagrid = new EasyuiDatagrid<UserVO>();
		MybatisCriteria mybatisCriteria = new MybatisCriteria();
		mybatisCriteria.setCurrentPage(pager.getPage());
		mybatisCriteria.setPageSize(pager.getRows());
		mybatisCriteria.setCondition(BeanConvertUtil.bean2Map(query));
		List<User> userList = userMybatisDao.queryByPageList(mybatisCriteria);
		UserVO userVO = null;
		List<UserVO> UserVOList = new ArrayList<UserVO>();
		for (User user : userList) {
			userVO = new UserVO();
			BeanUtils.copyProperties(user, userVO);
			UserVOList.add(userVO);
		}
		datagrid.setTotal((long) userMybatisDao.queryByCount(mybatisCriteria));
		datagrid.setRows(UserVOList);
		return datagrid;
	}
	
	/**
	 * 取得管理表单所有资料
	 * @param session
	 * @return
	 */
	public Set<UserVO> getUserTreegrid() {
		Set<UserVO> userVOSet = new TreeSet<UserVO>();
		List<User> allUserList = userMybatisDao.queryListByAll();
		Collections.sort(allUserList, new UserComparator());// 排序
		for(User user : allUserList){
			if(StringUtils.isEmpty(user.getParentNodeId())){
				userVOSet.add(this.getUserTreegridNode(user, allUserList));
			}
		}
		return userVOSet;
	}

	/**
	 * 取得User TreeGrid所有子节点
	 * @param user
	 * @param allUserList
	 * @return
	 */
	private UserVO getUserTreegridNode(User user, List<User> allUserList) {
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(user, userVO);
		
		List<User> subUserList = new ArrayList<User>();
		for(User subUser : allUserList){
			if(user.getNodeId().equals(subUser.getParentNodeId())){
				subUserList.add(subUser);
			}
		}
		
		if(subUserList != null && subUserList.size() > 0){
			Collections.sort(subUserList, new UserComparator());// 排序
			
			UserVO subUserVo = null;
			Map<String, String> parent = null;
			List<UserVO> childrenList = new ArrayList<UserVO>();
			for (User subUser : subUserList) {
				subUserVo = this.getUserTreegridNode(subUser, allUserList);
				
				parent = new HashMap<String, String>();
				parent.put("parentNodeId", user.getNodeId());
				parent.put("parentName", user.getUserName());
				subUserVo.setParent(parent);
				childrenList.add(subUserVo);
			}
			
			if(childrenList.size() > 0){
				userVO.setState("closed");
				userVO.setChildren(childrenList);
			}
		}
		return userVO;
	}
	
	public Set<EasyuiTree> getUserTree() {
		Set<EasyuiTree> easyuiTreeSet = new TreeSet<EasyuiTree>();
		List<User> allUserList = userMybatisDao.queryListByAll();
			
		if(allUserList != null && allUserList.size() > 0){
			Collections.sort(allUserList, new UserComparator());// 排序
			for(User user : allUserList){
				if(StringUtils.isEmpty(user.getParentNodeId())){
					easyuiTreeSet.add(this.getUserTreeNode(user, allUserList));
				}
			}
		}
		return easyuiTreeSet;
	}

	/**
	 * 取得User Tree所有子节点
	 * @param user
	 * @param allUserList 
	 * @return
	 */
	private EasyuiTree getUserTreeNode(User user, List<User> allUserList) {
		EasyuiTree tree = new EasyuiTree();
		tree.setId(user.getNodeId());
		tree.setText(user.getUserName());
		List<User> subUserList = new ArrayList<User>();
		for(User subUser : allUserList){
			if(user.getNodeId().equals(subUser.getParentNodeId())){
				if(subUser.getId() == UserLoginUtil.getLoginUser().getId()){
					continue;
				}
				subUserList.add(subUser);
			}
		}
		
		if(subUserList != null && subUserList.size() > 0){
			Collections.sort(subUserList, new UserComparator());// 排序
			Set<EasyuiTree> subTreeSet = new TreeSet<EasyuiTree>();
			
			for (User subUser : subUserList) {
				subTreeSet.add(this.getUserTreeNode(subUser, allUserList));
			}
			if(subTreeSet.size() > 0){
				tree.setState("closed");
				tree.setChildren(subTreeSet);
			}
		}
		return tree;
	}
	
	@Transactional
	public Json addUser(UserForm userForm) throws Exception {
		Json json = new Json();
		UserLoginQuery userLoginQuery = new UserLoginQuery();
		userLoginQuery.setId(userForm.getUserId());
		UserLogin userLogin = userLoginMybatisDao.queryById(userLoginQuery);
		if(userLogin == null){
			User user = new User();
			BeanUtils.copyProperties(userForm, user);
			user.setId(userForm.getUserId());
			user.setNodeId(RandomUtil.getUUID().replaceAll("-", ""));
			
			if(userForm.getUserType() > 0){
				String pwd = null;
				if(StringUtils.isNotEmpty(userForm.getCosPassword())){
					pwd = userForm.getCosPassword();
				}else{
					pwd = RandomUtil.genPassword(5);
				}
				user.setPwd(EncryptUtil.md5AndSha("123456"));
				//user.setPwd(EncryptUtil.md5AndSha(pwd));
				if(StringUtils.isNotEmpty(userForm.getCountryId())){
					user.setCountry((Country) countryMybatisDao.queryById(Integer.parseInt(userForm.getCountryId())));
				}
				user.setCreateTime(new Date());
				
				Set<Role> roleSet = new HashSet<Role>();
				if(StringUtils.isEmpty(userForm.getRole())){
					String[] roleIdArray = userForm.getRole().split(",");
					for(String roleId : roleIdArray){
						RoleQuery roleQuery = new RoleQuery();
						roleQuery.setId(roleId);
						roleSet.add(roleMybatisDao.queryMenuListById(roleQuery));
					}
				}else{
					RoleQuery roleQuery = new RoleQuery();
					roleQuery.setRoleName("客户子帐号");
					roleSet.add(roleMybatisDao.queryUniqueIdByName(roleQuery));
				}
				user.setRoleSet(roleSet);
				Set<Customer> customerSet = new HashSet<Customer>();
				if(StringUtils.isNotEmpty(userForm.getCustomer())){
					String[] customerIdArray = userForm.getCustomer().split(",");
					for(String customerId : customerIdArray){
						CustomerQuery customerQuery = new CustomerQuery();
						customerQuery.setId(customerId);
						customerSet.add(userMybatisDao.queryCustomerById(customerQuery));
					}
				}
				user.setCustomerSet(customerSet);
				/*if(StringUtils.isNotEmpty(userForm.getEmail())){
					MailVO mailVO = new MailVO();
					mailVO.setTo(userForm.getEmail());
					mailVO.setSubject("密码信");
					mailVO.setContent(String.format(ResourceUtil.getMailPwdTemplate(), user.getUserName(), pwd));
					MailUtil.sendMail(mailVO, mailSender);
				}*/
			}
			userMybatisDao.add(user);
			if(userForm.getUserType() > 0){
				if(StringUtils.isNotEmpty(userForm.getRole())){
					userMybatisDao.deleteRoleByUser(user);
					userMybatisDao.addRoleByUser(user);
				}
				if(StringUtils.isNotEmpty(userForm.getCustomer())){
					userMybatisDao.deleteCustomerByUser(user);
					userMybatisDao.addCustomerByUser(user);
				}
			}
			json.setSuccess(true);
			json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		}else{
			json.setSuccess(false);
			json.setMsg("此帐号已经被注册过！");
		}
		return json;
	}
	
	@CacheEvict(value = "btnCache", allEntries = true)
	@Transactional
	public Json editUser(UserForm userForm) {
		Json json = new Json();
		UserLogin userLogin = new UserLogin();
		userLogin.setId(userForm.getUserId());
		User user = userMybatisDao.queryListById(userLogin);
		BeanUtils.copyProperties(userForm, user);
		
		if(userForm.getUserType() > 0){
			if(StringUtils.isNotEmpty(userForm.getCosPassword())){
				user.setPwd(EncryptUtil.md5AndSha(userForm.getCosPassword()));
			}
			if(StringUtils.isNotEmpty(userForm.getCountryId())){
				user.setCountry((Country) countryMybatisDao.queryById(Integer.parseInt(userForm.getCountryId())));
			}
			if(StringUtils.isNotEmpty(userForm.getRole())){
				Set<Role> roleSet = new HashSet<Role>();
				String[] roleIdArray = userForm.getRole().split(",");
				for(String roleId : roleIdArray){
					RoleQuery roleQuery = new RoleQuery();
					roleQuery.setId(roleId);
					roleSet.add(roleMybatisDao.queryMenuListById(roleQuery));
				}
				user.setRoleSet(roleSet);
			}
			if(StringUtils.isNotEmpty(userForm.getCustomer())){
				Set<Customer> customerSet = new HashSet<Customer>();
				String[] customerIdArray = userForm.getCustomer().split(",");
				for(String customerId : customerIdArray){
					CustomerQuery customerQuery = new CustomerQuery();
					customerQuery.setId(customerId);
					customerSet.add(userMybatisDao.queryCustomerById(customerQuery));
				}
				user.setCustomerSet(customerSet);
			}
		}
		userMybatisDao.update(user);
		if(userForm.getUserType() > 0){
			if(StringUtils.isNotEmpty(userForm.getRole())){
				userMybatisDao.deleteRoleByUser(user);
				userMybatisDao.addRoleByUser(user);
			}
			userMybatisDao.deleteCustomerByUser(user);
			if(StringUtils.isNotEmpty(userForm.getCustomer())){
				userMybatisDao.addCustomerByUser(user);
			}
		}
		json.setSuccess(true);
		json.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));
		return json;
	}

	public Json deleteUser(String id) {
		Json json = new Json();
		UserLogin userLogin = userLoginMybatisDao.queryById(id);
		if(userLogin != null && userLogin.getUserType() == 0){
			/*UserLogin tempUserLogin = userLoginMybatisDao.queryById("temp");*/
			/*List<UserLogin> subUserLoginList = userLoginMybatisDao.findChildrenByParentNodeId(userLogin.getNodeId());
			for(UserLogin subUserLogin : subUserLoginList){
				subUserLogin.setParentNodeId(tempUserLogin.getNodeId());
				userLoginMybatisDao.update(subUserLogin);
			}*/
			userLoginMybatisDao.delete(userLogin);
		}
		json.setSuccess(true);
		return json;
	}
	
	public UserVO getUser(UserLogin userLogin) {
		UserVO userVO = null;
		if(userLogin != null){
			User user = userMybatisDao.queryListById(userLogin);
			userVO = new UserVO();
			BeanUtils.copyProperties(user, userVO);
		}
		return userVO;
	}

	public List<EasyuiCombobox> getSupervisorCombobox() {
		List<EasyuiCombobox> comboboxList = new ArrayList<EasyuiCombobox>();
		/*EasyuiCombobox combobox = null;*/
		/*UserLogin userLogin = userLoginMybatisDao.queryById("supervisor");*/
		/*List<UserLogin> userLoginList = userLoginMybatisDao.findChildrenByParentNodeId(userLogin.getNodeId());
		if(userLoginList != null && userLoginList.size() > 0){
			for(UserLogin subUserLogin : userLoginList){
				combobox = new EasyuiCombobox();
				combobox.setId(String.valueOf(subUserLogin.getId()));
				combobox.setValue(subUserLogin.getUserName());
				comboboxList.add(combobox);
			}
		}*/
		return comboboxList;
	}

	public List<EasyuiCombobox> getCustomerCombobox() {
		List<EasyuiCombobox> comboboxList = new ArrayList<EasyuiCombobox>();
		EasyuiCombobox combobox = null;
		List<Customer> customerIdList = userMybatisDao.queryCustomerByAll();
		if(customerIdList != null && customerIdList.size() > 0){
			for(Customer customerId : customerIdList){
				combobox = new EasyuiCombobox();
				combobox.setId(customerId.getId());
				combobox.setValue(customerId.getCustomerName());
				comboboxList.add(combobox);
			}
		}
		return comboboxList;
	}
}
