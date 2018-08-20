package com.cms.service;

import java.awt.image.BufferedImage;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cms.dao.CountryDao;
import com.cms.dao.UserDao;
import com.cms.dao.UserLoginDao;
import com.cms.entity.Country;
import com.cms.entity.User;
import com.cms.entity.UserLogin;
import com.cms.query.UserLoginQuery;
import com.cms.utils.EncryptUtil;
import com.cms.utils.MailUtil;
import com.cms.utils.RandomUtil;
import com.cms.utils.ResourceUtil;
import com.cms.utils.vo.MailVO;
import com.cms.vo.Json;
import com.cms.vo.form.ForgetPwdForm;
import com.cms.vo.form.LoginForm;
import com.cms.vo.form.UserOwenerForm;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

@Service("loginService")
public class LoginService extends BaseService{
	
	@Autowired
	private UserDao userMybatisDao;
	@Autowired
	private UserLoginDao userLoginMybatisDao;
	@Autowired
	private CountryDao countryMybatisDao;
	@Autowired
	private Producer captchaProducer;
	@Autowired(required = true)
	private JavaMailSender mailSender;
	
	/**
	 * 登录验证
	 * @param session 
	 * @param form
	 * @return
	 */
	public Json login(HttpSession session, LoginForm form) {
		Json json = new Json();
		UserLoginQuery userLoginQuery = new UserLoginQuery();
		userLoginQuery.setId(form.getUsername());
		UserLogin userLogin = userLoginMybatisDao.queryById(userLoginQuery);
		if(userLogin != null && userLogin.getPwd().equals(EncryptUtil.md5AndSha(form.getPassword()))){
			if(userLogin.getEnable().intValue() == 0){
				json.setMsg("此帐号已停止使用");
			}else{
				session.setAttribute(ResourceUtil.getUserInfo(), userLogin);
				userLogin.setLastLoginTime(new Date());
				userLogin.setSessionId(session.getId());
				userLoginMybatisDao.update(userLogin);
				json.setObj(userLogin);
				json.setSuccess(true);
				json.setMsg("登入成功！");
			}
		}else{
			json.setMsg("帐号或密码输入错误，登入失败！");
		}
		return json;
	}
	
	/**
	 * APP登录验证(不考虑session)
	 * @param form
	 * @return
	 */
	public Json appLogin(LoginForm form) {
		Json json = new Json();
		UserLoginQuery userLoginQuery = new UserLoginQuery();
		userLoginQuery.setId(form.getUsername());
		UserLogin userLogin = userLoginMybatisDao.queryAppLoginById(userLoginQuery);
		if(userLogin != null && userLogin.getPwd().equals(EncryptUtil.md5AndSha(form.getPassword()))){
			if(userLogin.getEnable().intValue() == 0){
				json.setSuccess(false);
				json.setMsg("此帐号已停止使用");
			}else{
				json.setSuccess(true);
				json.setMsg("登入成功！");
				json.setObj(userLogin);
			}
		}else{
			json.setSuccess(false);
			json.setMsg("帐号或密码输入错误，登入失败！");
		}
		return json;
	}

	public Json editUser(HttpSession session, UserOwenerForm userOwenerForm) throws Exception {
		Json json = new Json();
		UserLoginQuery userLoginQuery = new UserLoginQuery();
		userLoginQuery.setId(userOwenerForm.getUserId());
		UserLogin userLogin = new UserLogin();
		userLogin = userLoginMybatisDao.queryById(userLoginQuery);
		if(userLogin.getPwd().equals(EncryptUtil.md5AndSha(userOwenerForm.getPwd()))){
			String oldPwd = userLogin.getPwd();
			Integer enable = userLogin.getEnable();
			BeanUtils.copyProperties(userOwenerForm, userLogin);
			if(!StringUtils.isEmpty(userOwenerForm.getNewPwd()) && 
				!StringUtils.isEmpty(userOwenerForm.getRePwd()) &&
				 userOwenerForm.getNewPwd().equals(userOwenerForm.getRePwd())){
				userLogin.setPwd(EncryptUtil.md5AndSha(userOwenerForm.getNewPwd()));
			}else{
				userLogin.setPwd(oldPwd);
			}
			userLogin.setCountry((Country) countryMybatisDao.queryById(userOwenerForm.getCountryId()));
			userLogin.setEnable(enable);
			userLoginMybatisDao.update(userLogin);
			
			userLogin = (UserLogin)session.getAttribute(ResourceUtil.getUserInfo());
			session.setAttribute(ResourceUtil.getUserInfo(), userLogin);
			json.setSuccess(true);
			json.setMsg(ResourceUtil.getProcessResultMsg(true));
		}else{
			json.setMsg("密码输入错误！");
		}
		json.setObj(userLogin);
		return json;
	}

	public Json forgetPwd(HttpSession session, ForgetPwdForm form) throws Exception {
		Json json = new Json();
		if(form.getCode().equals(session.getAttribute(Constants.KAPTCHA_SESSION_KEY).toString())){
			UserLogin userLogin = new UserLogin();
			userLogin.setId(form.getUserId());
			User user = userMybatisDao.queryListById(userLogin);
			if(user == null){
				json.setMsg("查无此帐号！");
			}else{
				String pwd = RandomUtil.genPassword(5);
				user.setPwd(EncryptUtil.md5AndSha(pwd));
				userMybatisDao.update(user);
				
				MailVO mailVO = new MailVO();
				mailVO.setTo(user.getEmail());
				mailVO.setSubject("密码信");
				mailVO.setContent(String.format(ResourceUtil.getMailPwdTemplate(), user.getUserName(), pwd));
				MailUtil.sendMail(mailVO, mailSender);
				
				json.setSuccess(true);
				json.setMsg("请至注册时使用的信箱收取密码信！");
			}
		}else{
			json.setMsg("验证码输入错误！");
		}
		return json;
	}
	
	public void getCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		String capText = captchaProducer.createText();
		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			if(out != null){
				out.close();
			}
		}
	}
}
