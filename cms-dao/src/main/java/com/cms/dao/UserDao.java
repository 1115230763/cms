package com.cms.dao;


import java.util.List;

import com.cms.dao.BaseDao;
import com.cms.entity.Customer;
import com.cms.entity.User;
import com.cms.entity.UserLogin;
import com.cms.query.CustomerQuery;
/**
 * 
 * <br>
 * <b>功能：</b>UserDao<br>
 */
public interface UserDao extends BaseDao {

	public User queryListById(UserLogin userLogin);

	public Customer queryCustomerById(CustomerQuery customerQuery);

	public List<Customer> queryCustomerByAll();

	public void addRoleByUser(User user);

	public void deleteRoleByUser(User user);

	public void addCustomerByUser(User user);

	public void deleteCustomerByUser(User user);
	
}
