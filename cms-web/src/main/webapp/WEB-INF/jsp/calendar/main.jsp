<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<c:import url="/WEB-INF/jsp/include/meta.jsp" /> 
<c:import url="/WEB-INF/jsp/include/easyui.jsp" /> 
<script type="text/javascript" src="<c:url value="/js/jquery-easyui/easyui.custom.calendar.js"/>"></script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options='region:"center",border:false' style="overflow: hidden;">
			<div class="easyui-fullCalendar" data-options='fit:true'></div>
		</div>
	</div>
</body>
</html>