<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<c:import url="/WEB-INF/jsp/include/meta.jsp" /> 
<c:import url="/WEB-INF/jsp/include/easyui.jsp" /> 
<script type="text/javascript" charset="UTF-8">
$(function () { 
	if(window.top != window.self){//判断当前窗口是否在一个框架中
		if ('${isShowLoginDialog}') {
			window.top.showLogoutMsgDialog("${msg}");
		} else {
			window.top.location.replace('<c:url value="/index.html"/>');
		}
	}else{
		window.top.location.replace('<c:url value="/index.html"/>');
	}
});
</script>
</head>
<body>
</body>
</html>