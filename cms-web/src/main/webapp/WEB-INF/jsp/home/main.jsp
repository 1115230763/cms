<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<!-- <style type="text/css">
	#allmap {overflow: hidden;margin:0;background:url('<c:url value="/images/main.png"/>')no-repeat;}
</style> -->
<c:import url="/WEB-INF/jsp/include/meta.jsp" /> 
<c:import url="/WEB-INF/jsp/include/easyui.jsp" /> 
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div style="overflow:hidden;margin:0;text-align:center;background:url('<c:url value="/images/main.png"/>')no-repeat;background-size: cover;" data-options="region:'center',border:false">
		<div style='color:silver;font-family:"微软雅黑";font-weight:300;margin-top:8%'>
		</div>
		<%-- <div style='margin-top:17%'>
			<img src='<c:url value="/images/menuImg/logo.png"/>'/>
		</div> --%>
	</div>
</body>
</html>