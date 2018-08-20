<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<c:import url="/WEB-INF/jsp/include/meta.jsp" /> 
<c:import url="/WEB-INF/jsp/include/easyui.jsp" /> 
<link rel="stylesheet" type="text/css" href='<c:url value="/css/baseStyle.css"/>'/>
<script type="text/javascript">
var processType;
var ezuiMenu;
var ezuiForm;
var ezuiDialog;
var ezuiTreegrid;
$(function() {
	$(document).ajaxComplete(function() {
		parent.menu.tree('reload');
	});
	
	ezuiMenu = $('#ezuiMenu').menu();
	ezuiForm = $('#ezuiForm').form();
	ezuiTreegrid = $('#ezuiTreegrid').treegrid({
		url : '<c:url value="/userController.do?showTreegrid"/>',
		toolbar:"#toolbar",
		title:"<spring:message code='menu.datagrid.title'/>",
		pagination:false,
		fit:true,
		border:false,
		fitColumns:true,
    	nowrap:true,  
    	striped:true, 
		rownumbers:true, 
		collapsible:false,
		idField : 'id',
		treeField : 'userName',
		columns : [[
			{field: 'userName',		title: "名称",	width: 120 },
			{field: 'gender',		title: "性别",	width: 40 ,
				formatter:function(value,rowData,rowIndex){
					if(value == "M"){
						return "男";
					}else if(value == "F"){
						return "女";
					}else{
						return "";
					}
                }
			},
			{field: 'enable',		title: "使用中",	width: 60 ,
				formatter:function(value,rowData,rowIndex){
					if(value == 1){
						return "是";
					}else if(value == 0){
						return "否";
					}else{
						return "";
					}
                }
			},
			{field: 'birthday',		title: "生日",	width: 80 },
			{field: 'country',	title: "国籍",		width: 60 ,
				formatter:function(value,rowData,rowIndex){
					if(value){
						return value.countryName;
					}else{
						return "";
					}
                }
			},
			{field: 'email',		title: "邮箱",			width: 160 },
			{field: 'createTime',	title: "创建日期",	width: 120 },
			{field: 'lastLoginTime',title: "最后登录日期",	width: 120 },
			{field:	'roleSet',		title: "权限",		width: 70,
				formatter:function(value,rowData,rowIndex){
					var result = "";
					if(value){
						for(var i = 0; i < value.length; i++){
							result = result + value[i].roleName;
							if(i != (value.length-1)){
								result = result + "<br/>";
							}
	                    }
					}
                    return result;
                }
			},
			{field:	'customerSet',		title: "客户",		width:70,
				formatter:function(value,rowData,rowIndex){
					var result = "";
					if(value){
						for(var i = 0; i < value.length; i++){
							result = result + value[i].customerName;
							if(i != (value.length-1)){
								result = result + "<br/>";
							}
	                    }
					}
                    return result;
                }
			}
	    ]],
		onDblClickCell: function(index,field,value){
			edit();
		},
		onContextMenu : function(e, row) {
			 e.preventDefault();  
			 $(this).treegrid('unselectAll');
			 $(this).treegrid('select', row.id);
			 ezuiMenu.menu('show', {  
                left: e.pageX,  
                top: e.pageY  
            });
		},
		onLoadSuccess:function(data){ 
			ajaxBtn($('#menuId').val(), '<c:url value="/userController.do?getBtn"/>', ezuiMenu);
			ezuiTreegrid.treegrid('expandAll');
		}
	});

	ezuiDialog = $('#ezuiDialog').dialog({
		modal : true,
		title : "<spring:message code='adminUser.easyuiDialog.title'/>",
		buttons : '#ezuiDialogBtn',
		onClose : function() {
			ezuiFormClear(ezuiForm);
		},
		onOpen : function(){
			$('#userId').textbox('readonly', processType == 'edit');
		}
	}).dialog('close');
});

var add = function(){
	processType = 'add';
	ezuiDialog.dialog('open');
};

var edit = function(){
	processType = 'edit';
	var row = ezuiTreegrid.treegrid('getSelected');
	if(row){
		if(row.userType == 0){
			$('#enableCombobox').combobox('disable');
		    $('#genderCombobox').combobox('disable');
		    $('#countryCombobox').combobox('disable');
		    $('#roleCombobox').combobox('disable');
		    $('#customerCombobox').combobox('disable');
			$('#birthday').datebox('disable');
			$('#email').textbox('disable');
			ezuiForm.form('load',{
				userId : row.id,
				userName : row.userName,
				userType : row.userType
			});
		}else{
			$('#enableCombobox').combobox('enable');
			$('#genderCombobox').combobox('enable');
			$('#countryCombobox').combobox('enable');
			$('#roleCombobox').combobox('enable');
		    $('#customerCombobox').combobox('enable');
			$('#birthday').datebox('enable');
			$('#email').textbox('enable');
			ezuiForm.form('load',{
				userId : row.id,
				userName : row.userName,
				userType : row.userType,
				gender : row.gender,
				enable : row.enable,
				birthday : row.birthday,
				countryId : row.country.id,
				email : row.email
			});
			var role = row.roleSet;
			if(role){
				var roleArray = [];
				for(var i = 0; i < role.length; i++){
					roleArray[i] = role[i].id;
			    }
				$('#roleCombobox').combobox('setValues',roleArray);
			}
			var customer = row.customerSet;
			if(customer){
				var customerArray = [];
				for(var i = 0; i < customer.length; i++){
					customerArray[i] = customer[i].id;
			    }
				$('#customerCombobox').combobox('setValues',customerArray);
			}
		}
		$('#parentNodeId').combotree('setValue', row.parent ? row.parent.parentNodeId : "");
		ezuiDialog.dialog('open');
	}else{
		$.messager.show({
			msg : "<spring:message code='common.message.selectRecord'/>", title : "<spring:message code='common.message.prompt'/>"
		});
	}
};

var del = function(){
	var row = ezuiTreegrid.treegrid('getSelected');
	if(row){
		if(row.userType == 0){
			$.messager.confirm("<spring:message code='common.message.confirm'/>", "<spring:message code='common.message.confirm.delete'/>", function(confirm) {
				if(confirm){
					$.ajax({
						url : '<c:url value="userController.do?delete"/>',
						data : {id : row.id},
						type : 'POST', 
						dataType : 'JSON',
						success : function(result){
							var msg = "";
							try {
								msg = result.msg;
							} catch (e) {
								msg = "<spring:message code='common.message.data.delete.failed'/>";
							} finally {
								$.messager.show({
									msg : msg, title : "<spring:message code='common.message.prompt'/>"
								});
								ezuiTreegrid.treegrid('reload');
							}
						}
					});
				}
			});
		}else{
			$.messager.show({
				msg : "无法刪除使用者！", title : "<spring:message code='common.message.prompt'/>"
			});
		}
	}else{
		$.messager.show({
			msg : "<spring:message code='common.message.selectRecord'/>", title : "<spring:message code='common.message.prompt'/>"
		});
	}
};

var commit = function(){
	var url = "";
	if (processType == "edit") {
		url = '<c:url value="/userController.do?edit"/>';
	}else{
		url = '<c:url value="/userController.do?add"/>';
	}

	ezuiForm.form('submit', {
		url : url,
		onSubmit : function(){
			if(ezuiForm.form('validate')){
				$.messager.progress({
					text : "<spring:message code='common.message.data.processing'/>", interval : 100
				});
				return true;
			}else{
				return false;
			}
		},
		success : function(data) {
			try {
				var result = $.parseJSON(data);
				if(result.success){
					if($('#userId').val() == "${sessionScope.userInfo.id}"){
						parent.$('#userInfo').text("<spring:message code='adminUser.text.hello'/>["+$('#userName').val()+"]");
					}
					ezuiTreegrid.treegrid('reload');
					ezuiDialog.dialog('close');
				}
				$.messager.show({
					msg : result.msg, title : "<spring:message code='common.message.prompt'/>"
				});
			} catch (e) {
				$.messager.show({
					msg : "<spring:message code='common.message.data.process.failed'/><br/><br/>"+ data, title : "<spring:message code='common.message.prompt'/>"
				});
			} finally {
				$.messager.progress('close');
			}
		}
	});
};

var clearParentName = function(){
	$('#parentNodeId').combotree('clear');
};
</script>

</head>
<body>
	<input type="hidden" id="menuId" name="menuId" value="${menuId}"/>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options='region:"center",border:false' style="overflow: hidden;">
			<div id="toolbar" class="datagrid-toolbar" style="padding: 5px;">
				<div>
					<a onclick="add();" 	id="ezuiBtn_add"	class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-add"'		href="javascript:void(0);"><spring:message code="common.button.add"/></a>
					<a onclick="edit();" 	id="ezuiBtn_edit"	class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-edit"'		href="javascript:void(0);"><spring:message code="common.button.edit"/></a>
					<a onclick="clearTreegridSelected('#ezuiTreegrid');" class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-undo"'	href="javascript:void(0);"><spring:message code="common.button.cancelSelect"/></a>
				</div>
			</div>
			
			<table id="ezuiTreegrid"></table> 
		</div>
	</div>
	
	<div id="ezuiDialog" style="padding: 10px;">
		<form id="ezuiForm" method="post">
			<table>
				<tr>
					<th>类型</th>
					<td>
						<input type="text" id="userTypeCombobox" name="userType" class="easyui-combobox" size='16' data-options="	panelHeight:'auto',
																																	editable:false,
																																	required:true,
																																	valueField: 'id',
																																	textField: 'value',
																																	data: [
																																		{id: '0', value: '部门'},
																																		{id: '1', value: '员工'}
																																	],
																																	onSelect : function(record){
																																		if(record.id == 0){
																																		    $('#enableCombobox').combobox('disable');
																																		    $('#genderCombobox').combobox('disable');
																																		    $('#countryCombobox').combobox('disable');
																																		    $('#roleCombobox').combobox('disable');
																																		    $('#customerCombobox').combobox('disable');
																																			$('#birthday').datebox('disable');
																																			$('#email').textbox('disable');
																																		}else{
																																			$('#enableCombobox').combobox('enable');
																																			$('#genderCombobox').combobox('enable');
																																			$('#countryCombobox').combobox('enable');
																																			$('#roleCombobox').combobox('enable');
																																		    $('#customerCombobox').combobox('enable');
																																			$('#birthday').datebox('enable');
																																			$('#email').textbox('enable');
																																		}
																																	}"/> 
				 	</td>
					<th>账号</th>
					<td><input type="text" id="userId" name="userId" class="easyui-textbox" size='16' data-options='required:true'/></td>
					<th>名称</th>
					<td><input type="text" id="userName" name="userName" class="easyui-textbox" size='16' data-options='required:true'/></td>
					<th>国籍</th>
					<td>
						<input type="text" id="countryCombobox" name="countryId" class="easyui-combobox" size='16' data-options="	required:true,
																																	url:'<c:url value="/countryController.do?getCountryCombobox"/>',
																																	valueField:'id',
																																	textField:'value'
																																 "/>  
					</td>
				</tr>
				<tr>
					<th>性别</th>
					<td>
						<input type="text" id="genderCombobox" name="gender" class="easyui-combobox" size='16' data-options="	panelHeight:'auto',
																																editable:false,
																																required:true,
																																valueField: 'id',
																																textField: 'value',
																																data: [
																																	{id: 'M', value: '<spring:message code="adminUser.datagrid.gender.m"/>'},
																																	{id: 'F', value: '<spring:message code="adminUser.datagrid.gender.f"/>'}
																																]"/>  
					</td>
					<th>生日</th>
					<td><input type="text" id="birthday" name="birthday" class="easyui-datebox" size='16' data-options='required:true' size="15"/></td>
					<th>邮箱</th>
					<td colspan="3"><input type="text" id="email" name="email" class="easyui-textbox" size='42' data-options='required:true,validType:"email"'/></td>
				</tr>
				<tr>
					<th>使用中</th>
					<td>
						<input type="text" id="enableCombobox" class="easyui-combobox" name="enable" size='16' data-options="	panelHeight:'auto',
																																editable:false,
																																required:true,
																																valueField: 'id',
																																textField: 'value',
																																data: [
																																	{id: '1', value: '是'},
																																	{id: '0', value: '否'}
																																]"/>  
					</td>
					<th>父节点</th>
					<td colspan="3">
						<input type="text" id="parentNodeId" name="parentNodeId" class="easyui-combotree" size='16' data-options=" 	url : '<c:url value="/userController.do?getTree"/>',
																																animate : true,
																																lines : true,
																																onLoadSuccess : function(node, data) {
																																	var t = $(this);
																																	if (data) {
																																		$(data).each(function(index, d) {
																																			if (this.state == 'closed') {
																																				t.tree('expandAll');
																																			}
																																		});
																																	}
																																}">
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-remove"' onclick="clearParentName();">
							<spring:message code="common.button.clear"/>
						</a>
					</td>
				</tr>
				<tr>
					<th>权限</th>
					<td colspan="7">
						<input type="text" id="roleCombobox" name="role" class="easyui-combobox" size='95' data-options="required:true,
																														panelHeight:200,
																														multiple:'multiple',
																														editable:false,
																														url:'<c:url value="/roleController.do?getCombobox"/>',
																														valueField:'id',  
																														textField:'value'" />  
					</td>
				</tr>
				<tr>
					<th>客户</th>
					<td colspan="5">
						<input type="text" id="customerCombobox" name="customer" class="easyui-combobox" size='70' style="height:60px" data-options="panelHeight:200,
																																					multiple:'multiple',
																																					multiline:true,
																																					editable:false,
																																				    url:'<c:url value="/userController.do?getCustomerCombobox"/>',
																																				    valueField:'id',  
																																				    textField:'value'" />  
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="ezuiDialogBtn">
		<a onclick="commit();" id="ezuiBtn_commit" class="easyui-linkbutton" href="javascript:void(0);"><spring:message code="common.button.commit"/></a>
		<a onclick="ezuiDialogClose('#ezuiDialog');" class="easyui-linkbutton" href="javascript:void(0);"><spring:message code="common.button.close"/></a>
	</div>
	
	<div id="ezuiMenu" class="easyui-menu" style="width:120px;display: none;">
		<div onclick="add();" id="menu_add"		data-options='plain:true,iconCls:"icon-add"'><spring:message code="common.button.add"/></div>
		<div onclick="edit();"id="menu_edit" 	data-options='plain:true,iconCls:"icon-edit"'><spring:message code="common.button.edit"/></div>
	</div> 
</body>
</html>