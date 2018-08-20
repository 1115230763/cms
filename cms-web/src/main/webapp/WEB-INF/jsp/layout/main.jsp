<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<title>虫妈网络仓储管理系统</title>
<c:import url='/WEB-INF/jsp/include/meta.jsp' /> 
<c:import url='/WEB-INF/jsp/include/easyui.jsp' />
<link rel="stylesheet" type="text/css" href='<c:url value="/css/menu.css"/>'/>
<script type="text/javascript" charset="UTF-8">
var ezuiTabs;
var logoutForm;
var loginForm;
var modifyForm;
var forgetPwdForm;
var loginDialog;
var modifyDialog;
var forgetDialog;
var menu;
$(function () { 
	$('#loginDialog input').keyup(function(event) {
		if (event.keyCode == '13') {
			login();
		}
	});
	loginForm = $('#loginForm').form();
	logoutForm = $('#logoutForm').form();
	modifyForm = $('#modifyForm').form();
	forgetPwdForm = $('#forgetPwdForm').form();
	loginDialog = $('#loginDialog').dialog({
		modal : true,
		title : '使用者登入',
		closable : false,
		cache: false,
		buttons : '#loginDialogBtn',
		onClose : function() {
			ezuiFormClear(forgetPwdForm);
		}
	}).dialog('close');
	
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
	
	modifyDialog = $('#modifyDialog').show().dialog({
		modal : true,
		cache: false,
		title : '修改密码',
		buttons : '#modifyDialogBtn',
		onClose : function() {
			ezuiFormClear(modifyForm);
		}
	}).dialog('close');

	if(!'${sessionScope.userInfo.userName}'){
		openLoginDialog();
	}
	ezuiTabs = $('#ezuiTabs').tabs({
		fit : true,
		border : false
	});
	if('${sessionScope.userInfo}'){
		addTab({url : "<c:url value='/home.html'/>", title : "首页",closable : false});
	}
});
var addTab = function (params) {
	var iframe = '<iframe src="' + params.url + '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
	var opts = {
		title : params.title,
		closable : params.closable,
		iconCls : params.iconCls,
		content : iframe,
		border : false,
		fit : true
	};
	if (ezuiTabs.tabs('exists', opts.title)) {
		ezuiTabs.tabs('select', opts.title);
	} else {
		ezuiTabs.tabs('add', opts);
	}
};

var getMenu = function(){
	menu = $('#menu').tree({
		url : '<c:url value="/menuController.do?showTree"/>',
		animate : false,
		onClick : function(node) {
			if (node.attributes && node.attributes.src && node.attributes.src.length > 0) {
				var href;
				if (/^\//.test(node.attributes.src)) {
					href = sy.bp() + node.attributes.src;
				} else {
					href = node.attributes.src;
				}
				if(node.attributes.type == "1"){
					addTab({
						url : href + "&menuId=" + node.attributes.menuId,
						title : node.text,
						iconCls : node.iconCls,
						closable : true
					});
				}
			} 
		},
		onLoadSuccess : function(node, data) {
			if (data) {
				$(data).each(function(index, d) {
					if (this.state == 'closed') {
						menu.tree('expandAll');
					}
				});
			}
		}
	});
};

var openLoginDialog = function(){
	window.top.location.replace('<c:url value="/index.html"/>');
};

var login = function(){
	loginForm.form('submit',{
		url : '<c:url value="/loginController.do?login"/>',
		success : function(data) {
			var result = $.parseJSON(data);
			if (result.success) {
				addTab({url : "<c:url value='/home.html'/>", title : "首页",closable : false});
				getMenu();
				$("#northFrame").contents().find('#userInfo').text(sy.fs('您好！[{0}]', result.obj.userName));
				$("#northFrame").contents().find('#ezuiForm').css('display','block');
				loginDialog.dialog('close');
			}
			/* $.messager.show({
				msg : result.msg,
				title : "<spring:message code='common.message.prompt'/>"
			}); */
		}
	});
};
var logout = function(){
	$.messager.confirm("<spring:message code='common.message.confirm'/>", '您是否要登出系統？', function(confirm) {
		if(confirm){
			logoutForm.form('submit', {
				url :'<c:url value="/logout.html"/>',
				success : function(data) {
					if(data){
						window.top.location.replace('<c:url value="/index.html"/>');
					}
				}
			});
		}
	});
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
					loginDialog.dialog('open');
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
	loginDialog.dialog('open');
};
var modifyCommit = function(){
	modifyForm.form('submit', {
		url : '<c:url value="/loginController.do?editUser"/>',
		onSubmit : function(){
			if(modifyForm.form('validate')){
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
			var result;
			try {
				result = $.parseJSON(data);
				msg = result.msg;
				$("#northFrame").contents().find('#userInfo').text(sy.fs('您好！[{0}]', result.obj.userName));
			} catch (e) {
				msg = "<spring:message code='common.message.data.process.failed'/><br/><br/>"+ data;
			} finally {
				$.messager.show({
					msg : msg, title : "<spring:message code='common.message.prompt'/>"
				});
				$.messager.progress('close');
				modifyDialog.dialog('close');
			}
		}
	});
};
var modifyCancel = function(){
	modifyDialog.dialog('close');
};
var showLogoutMsgDialog = function(msg){
	$('#logoutMsgDialog').text(msg);
	logoutMsgDialog = $('#logoutMsgDialog').dialog({
		modal : true,
		closable : false,
		title : '登出',
		buttons : [{
			text : '确认',
			handler : function() {
				window.top.location.replace('<c:url value="/index.html"/>');
			}
		}]
	}).dialog('open');
};
var getCaptchaImage = function(){
	$('#captchaImage').attr('src',"<c:url value='/loginController.do?getCaptchaImage&time="+new Date().getTime()+"'/>");
};
var openForgetPwdDialog = function(){
	getCaptchaImage();
	loginDialog.dialog('close');
	forgetPwdDialog.dialog('open');
};
</script>
</head>
<body>
	<div class='easyui-layout' data-options='fit:true,border:false'>
		<div id="north" data-options='region:"north",border:true' style="overflow: hidden;height:40px;">
			<iframe src="<c:url value='/head.html'/>" id="northFrame" name="northFrame" style="border:0;width:100%;height:100%;"></iframe>
		</div>
		<div data-options='region:"center",border:true' style="overflow: hidden;">
			<div id="ezuiTabs" style="overflow: hidden;"></div>
		</div>
		<div id="west" data-options='region:"west",hideCollapsedContent:false,border:true,collapsed:false' title="菜单栏" style="width:220px;">
			<c:import url='/WEB-INF/jsp/layout/menu.jsp' /> 
		</div>  
	</div>
	<div style="dispaly:none;">
		<form id="logoutForm" method="post"></form>
	</div>
	<div id="loginDialog" style="padding: 10px;">
		<form id="loginForm" method="post">
			<table class="tableForm tableHeadDialogBase">
				<tr>
					<th>帐号</th>
					<td><input type="text" id="username" name="username" class="easyui-textbox" size='19' data-options="required:true" /></td>
				</tr>
				<tr>
					<th>密码</th>
					<td>
						<input type="password" name="password" class="easyui-textbox" size='19' data-options="required:true"/>
						<a onclick="openForgetPwdDialog();" href="javascript:void(0);">忘记密码</a>
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
	<div id="modifyDialog" style="ppadding: 10px;">
		<form id="modifyForm" method="post">
			<input type="hidden" name="userId"/>
			<table>
				<tr>
					<th>名称</th>
					<td><input type="text" id="userName" name="userName" class="easyui-textbox" size="19" data-options='required:true'/></td>
				</tr>
				<tr>
					<th>性别</th>
					<td>
						<input type="text" id="genderCombobox" name="gender" class="easyui-combobox" size="19" data-options="	panelHeight:'auto',
																																editable:false,
																																required:true,
																																valueField: 'id',
																																textField: 'value',
																																data: [
																																	{id: 'M', value: '男'},
																																	{id: 'F', value: '女'}
																																]
																															 "/>  
					</td>
				</tr>
				<tr>
					<th>生日</th>
					<td><input type="text" id="birthday" name="birthday" class="easyui-datebox" size="19" data-options='required:true' /></td>
				</tr>
				<tr>
					<th>国籍</th>
					<td>
						<input id="countryCombobox" name="countryId" class="easyui-combobox" size="19" data-options="	required:true,
																														url:'<c:url value="/countryController.do?getCountryCombobox"/>',
																														valueField:'id',
																														textField:'value'
																													 "/>  
					</td>
				</tr>
				<tr>
					<th>信箱</th>
					<td><input type="text" id="email" name="email" class="easyui-textbox" size="19" data-options='required:true,validType:"email"'/></td>
				</tr>
				<tr>
					<th>旧密码</th>
					<td><input type="password" id="pwd" name="pwd" class="easyui-textbox" size="19" data-options='required:true'/></td>
				</tr>
				<tr>
					<th>新密码</th>
					<td><input type="password" id="newPwd" name="newPwd" class="easyui-textbox" size="19" data-options="validType:['eqPassword[\'#rePwd\']']"/></td>
				</tr>
				<tr>
					<th>密码确认</th>
					<td><input type="password" id="rePwd" name="rePwd" class="easyui-textbox" size="19" data-options="validType:['eqPassword[\'#newPwd\']']"/></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="logoutMsgDialog" style="dispaly:none;padding: 10px;"></div>
	<div id="loginDialogBtn">
		<a onclick="login();" id="loginDialogBtn_commit" class="easyui-linkbutton" href="javascript:void(0);">提交</a>
	</div>
	<div id="forgetPwdDialogBtn">
		<a onclick="forgetPwdCommit();" id="forgetPwdDialogBtn_commit" class="easyui-linkbutton" href="javascript:void(0);">提交</a>
		<a onclick="forgetPwdCancel();" id="forgetPwdDialogBtn_cancel" class="easyui-linkbutton" href="javascript:void(0);">取消</a>
	</div>
	<div id="modifyDialogBtn">
		<a onclick="modifyCommit();" id="modifyCommit_commit" class="easyui-linkbutton" href="javascript:void(0);">提交</a>
		<a onclick="modifyCancel();" id="modifyCommit_cancel" class="easyui-linkbutton" href="javascript:void(0);">取消</a>
	</div>
</body>
</html>