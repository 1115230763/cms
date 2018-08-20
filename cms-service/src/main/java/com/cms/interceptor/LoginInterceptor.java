package com.cms.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cms.dao.UserLoginDao;
import com.cms.entity.UserLogin;
import com.cms.query.UserLoginQuery;
import com.cms.utils.RandomUtil;
import com.cms.utils.ResourceUtil;
import com.cms.utils.annotation.Login;
import com.cms.utils.annotation.RestWebservice;

/**
 * 登录验证拦截器
 * @Date 2016/7/1
 * @author hao
 */
public class LoginInterceptor implements HandlerInterceptor {

	private static final Logger logger = Logger.getLogger("");
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
	
	@Autowired
	private UserLoginDao userLoginMybatisDao;
	
	private long startTime = 0;
	/**
	 * 在controller之前拦截
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		startTime = System.currentTimeMillis();
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Login login = handlerMethod.getMethodAnnotation(Login.class);
		if (null == login) {//沒有Login注解，直接放行
			return true;
		}else{
			HttpSession session = request.getSession(false);
			if(session == null){
				request.setAttribute("msg", "闲置太久，系统自动登出，请重新登录！");
			}else{
				UserLogin sessionUserLogin = (UserLogin)session.getAttribute(ResourceUtil.getUserInfo());
				if(sessionUserLogin == null){
					request.setAttribute("msg", "查无登录资讯，请重新登录！");
					session.invalidate();
				}else{
					UserLoginQuery userLoginQuery = new UserLoginQuery();
					userLoginQuery.setId(sessionUserLogin.getId());
					UserLogin dbUserLogin = userLoginMybatisDao.queryById(userLoginQuery);
					if(dbUserLogin.getEnable().intValue() == 0 || !dbUserLogin.getSessionId().equals(session.getId())){
						if(dbUserLogin.getEnable().intValue() == 0){
							request.setAttribute("msg", "帐号已被停止只使用！");
						}else{
							request.setAttribute("msg", "帐号已在其他地方登入！");
						}
						session.invalidate();
					}else{
						session.setAttribute(ResourceUtil.getUserInfo(), dbUserLogin);
						return true;
					}
				}
			}
			request.setAttribute("isShowLoginDialog", true);
			request.getRequestDispatcher("/reLogin.html").forward(request, response);
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
		StringBuilder sb = new StringBuilder();
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Login login = handlerMethod.getMethodAnnotation(Login.class);
		RestWebservice restWebservice = handlerMethod.getMethodAnnotation(RestWebservice.class);
		if(login != null){
			UserLogin user = (UserLogin)request.getSession().getAttribute(ResourceUtil.getUserInfo());
			sb.append("使用者：")
			  .append(user == null ? "" : user.getId())
			  .append("，成功调用Controller，執行ID：Controller-");
		}else if(restWebservice != null){
			sb.append("使用者：")
			  .append(request.getParameter(ResourceUtil.getRESTLoginName()))
			  .append("，成功调用Webservice，執行ID：Webservice-");
		}else{
			sb.append("成功调用Controller，執行ID：Controller-");
		}
		
		sb.append(RandomUtil.getUUID());
		logger.info(sb.toString());sb.setLength(0);
		sb.append("执行类别：")
		  .append(handlerMethod.getBeanType().getName())
		  .append("，执行方法：")
		  .append(handlerMethod.getMethod().getName());
		logger.info(sb.toString());sb.setLength(0);
		sb.append("执行完成时间：")
		  .append(sdf.format(new Date()))
		  .append("，执行总耗时间：")
		  .append(System.currentTimeMillis() - startTime)
		  .append("ms");
		logger.info(sb.toString());sb.setLength(0);
		sb.append("==================================================================================================");
		logger.info(sb.toString());sb.setLength(0);
	}
}
