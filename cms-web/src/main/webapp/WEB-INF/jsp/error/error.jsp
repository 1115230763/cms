<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<c:import url="/WEB-INF/jsp/include/meta.jsp" /> 
<c:import url="/WEB-INF/jsp/include/easyui.jsp" /> 
</head>
<body class="easyui-layout" data-options='fit:true'>
	<div data-options='region:"center",border:false'>
		<table border="1" style="width: 100%">
			<tr>
				<td align="center">
					<font color="red">系统发生错误</font>
				</td>
			</tr>
			<tr>
				<td>
					${exceptionMessage}
				</td>
			</tr>
		</table>
	</div>
</body>
</html>