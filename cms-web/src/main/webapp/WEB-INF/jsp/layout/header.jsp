<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<c:import url="/WEB-INF/jsp/include/meta.jsp" /> 
<c:import url="/WEB-INF/jsp/include/easyui.jsp" />
<style type="text/css">
	.logout{
		color:#1b94ff;
	}
	.logout:hover{
		color:red;
	}
</style>
<script type="text/javascript" charset="UTF-8">
var ezuiForm;
$(function () { 
	ezuiForm = $('#ezuiForm').form();
	if('${sessionScope.userInfo}'){
		$('#ezuiForm').show();
		$('#userInfo').text(sy.fs('您好！[{0}]', '${sessionScope.userInfo.userName}'));
	}else{
		$('#ezuiForm').hide();
		$('#userInfo').text('');
	}
});

var edit = function(){
	$.ajax({
		url : 'userController.do?getUser',
		type : 'POST', 
		dataType : 'JSON',
		success : function(result){
			try {
				parent.modifyForm.form('load',{
					userId : result.id,
					userName : result.userName,
					gender : result.gender,
					birthday : result.birthday,
					countryId : result.country.id,
					email : result.email
				});
				parent.modifyDialog.dialog('open');
			} catch (e) {
				$.messager.show({
					msg : "<spring:message code='common.message.data.process.failed'/><br/><br/>", title : "<spring:message code='common.message.prompt'/>"
				});
			} 
		}
	});
};

var logout = function(){
	parent.logout();
}
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options='region:"center",border:false' style="overflow: hidden;background:url('<c:url value="/images/header.png"/>')no-repeat;background-size: cover;">
			<div style="position: absolute; right: 10px; top:20px;">
				<form id="ezuiForm" method="post">
					<span id="userInfo" style="font-family:'微软雅黑';margin-right:15px;color:#333333;"></span>
					<a onclick="edit();" style="font-family:'微软雅黑';margin-right:15px;color:#333333;" href="javascript:void(0);">修改密码</a>
					<a onclick="logout();" href="javascript:void(0);" ><span class="logout" style="font-family:'微软雅黑';margin-right:15px;color:#333333;">退出</span></a>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
