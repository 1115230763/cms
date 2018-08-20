<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<c:import url='/WEB-INF/jsp/include/meta.jsp' /> 
<c:import url='/WEB-INF/jsp/include/easyui.jsp' />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>虫妈网络仓储管理系统-登录</title>
<link rel="stylesheet" type="text/css" href='<c:url value="/css/login.css"/>'/>
<script type="text/javascript" charset="UTF-8">
var loginForm;
var forgetPwdForm;
var forgetDialog;
var menu;
$(function () { 
	$('#input').keyup(function(event) {
		if (event.keyCode == '13') {
			login();
		}
	});
	loginForm = $('#loginForm').form();
	forgetPwdForm = $('#forgetPwdForm').form();
	
	forgetPwdDialog = $('#forgetPwdDialog').show().dialog({
		modal : true,
		title : '忘记密码',
		closable : false,
		cache: false,
		buttons : '#forgetPwdDialogBtn',
		onClose : function() {
			ezuiFormClear(forgetPwdForm);
		}
	}).dialog('close');
});

var login = function(){
	loginForm.form('submit',{
		url : '<c:url value="/loginController.do?login"/>',
		success : function(data) {
			var msg = "";
			var result = $.parseJSON(data);
			if (result.success) {
				window.top.location.replace('<c:url value="/main.html"/>');
			} else {
				msg = result.msg;
				$.messager.show({
					msg : msg, title : "<spring:message code='common.message.prompt'/>"
				});
			}
		}
	});
};

var reset = function(){
	$("#loginForm #username").textbox('setValue','');
	$("#loginForm #password").textbox('setValue','');
};

var forgetPwdCommit = function(){
	forgetPwdForm.form('submit',{
		url : '<c:url value="/loginController.do?forgetPwd"/>',
		onSubmit : function(){
			if(forgetPwdForm.form('validate')){
				$.messager.progress({
					text : "<spring:message code='common.message.data.processing'/>", interval : 100
				});
				return true;
			}else{
				return false;
			}
		},
		success : function(data) {
			var msg = "";
			try {
				var result = $.parseJSON(data);
				if(result.success){
					forgetPwdDialog.dialog('close');
				}else{
					$('#code').val("");
					getCaptchaImage();
				}
				msg = result.msg;
			} catch (e) {
				msg = "<spring:message code='common.message.data.process.failed'/><br/><br/>" + data;
			} finally {
				$.messager.show({
					msg : msg, title : "<spring:message code='common.message.prompt'/>"
				});
				$.messager.progress('close');
			}
		}
	});
};
var forgetPwdCancel = function(){
	forgetPwdDialog.dialog('close');
};
var getCaptchaImage = function(){
	$('#captchaImage').attr('src',"<c:url value='/loginController.do?getCaptchaImage&time="+new Date().getTime()+"'/>");
};
var openForgetPwdDialog = function(){
	getCaptchaImage();
	forgetPwdDialog.dialog('open');
};
</script>
</head>
<body>
 	<div class="second_body">
		<form id="loginForm" method="post">
        	<div class="logo"></div>
            <div class="title-zh">虫妈网络仓储管理系统</div>
            <div class="title-en" style="">MMC Warehouse Management System</div>
            <div class="message" data-bind="html:message"></div>
			<table>
				<tr>
					<th>帐号</th>
					<td><input type="text" id="username" name="username" class="easyui-textbox" size='19' data-options="required:true" /></td>
				</tr>
				<tr>
					<th>密码</th>
					<td>
						<input type="password" id="password" name="password" class="easyui-textbox" size='19' data-options="required:true"/>
					</td>
					<td>
						<a onclick="openForgetPwdDialog();" href="javascript:void(0);">忘记密码</a>
					</td>
				</tr>
                <tr>
                    <td colspan="2" style="text-align:right">
                    	<input onclick="login();" type="submit" value="登录" class="login_button" />
                    </td>
                </tr>
			</table>
		</form>
	</div>
	<div id="forgetPwdDialog" style="padding: 10px;">
		<form id="forgetPwdForm" method="post">
			<table>
				<tr>
					<th>帐号</th>
					<td><input type="text" name="userId" class="easyui-textbox" data-options="required:true" /></td>
				</tr>
				<tr>
					<th>验证码</th>
					<td>
						<img id="captchaImage" alt="验证码"><br>
						<input id="code" name="code" type="text" class="easyui-textbox" data-options='required:true' maxlength="6"/>
						<a onclick="getCaptchaImage();" href="javascript:void(0);">
							<img alt="刷新验证码" src="<c:url value="/images/reload_2.png"/>" border=0>
						</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="forgetPwdDialogBtn">
		<a onclick="forgetPwdCommit();" id="forgetPwdDialogBtn_commit" class="easyui-linkbutton" href="javascript:void(0);">提交</a>
		<a onclick="forgetPwdCancel();" id="forgetPwdDialogBtn_cancel" class="easyui-linkbutton" href="javascript:void(0);">取消</a>
	</div>
</body>
</html>