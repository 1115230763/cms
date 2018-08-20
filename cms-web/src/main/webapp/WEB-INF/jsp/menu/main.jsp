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
		url : '<c:url value="/menuController.do?showTreegrid"/>',
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
		treeField : 'menuName',
		columns : [[  
				{field:'menuName',	title:"名称", width:120},  
				{field:'url',		title:"路径",	width:150},  
				{field:'displaySeq',title:"显示顺序", width:40},
				{field:'roleSet',	title:"权限", width:70,
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
		onLoadSuccess : function() {
			ajaxBtn($('#menuId').val(), '<c:url value="/menuController.do?getBtn"/>', ezuiMenu);
 			ezuiTreegrid.treegrid('expandAll');
		}
	});

	ezuiDialog = $('#ezuiDialog').dialog({
		modal : true,
		title : "<spring:message code='menu.easyuiDialog.title'/>",
		buttons : '#ezuiDialogBtn',
		onClose : function() {
			ezuiFormClear(ezuiForm);
		},
		onOpen : function(){
			ezuiCombotreeReload(ezuiForm);
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
		ezuiForm.form('load',{
			menuId : row.id,
			menuName : row.menuName,
			parentId : row.parent ? row.parent.menuId : "",
			displaySeq : row.displaySeq
		});
		var role = row.roleSet;
		var roleArray = [];
		for(var i = 0; i < role.length; i++){
			roleArray[i] = role[i].id;
	    }
		$('#roleCombobox').combobox('setValues',roleArray);
		$('#menuTypeCombobox').combobox('select',row.menuType);
		if(row.menuType == 0){
			$('#url').textbox('disable');
		}else{
			$('#url').textbox('enable');
			$('#url').textbox('setValue', row.url);
		}
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
		$.messager.confirm("<spring:message code='common.message.confirm'/>", "<spring:message code='common.message.confirm.delete'/>", function(confirm) {
			if(confirm){
				$.ajax({
					url : '<c:url value="menuController.do?delete"/>',
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
			msg : "<spring:message code='common.message.selectRecord'/>", title : "<spring:message code='common.message.prompt'/>"
		});
	}
};

var commit = function(){
	var url = "";
	if (processType == "edit") {
		url = '<c:url value="/menuController.do?edit"/>';
	}else{
		url = '<c:url value="/menuController.do?add"/>';
	}
	ezuiForm.form('submit', {
		url :url,
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
				$.messager.show({
					msg : result.msg, title : "<spring:message code='common.message.prompt'/>"
				});
			} catch (e) {
				$.messager.show({
					msg : "<spring:message code='common.message.data.process.failed'/><br/><br/>"+ data, title : "<spring:message code='common.message.prompt'/>"
				});
			} finally {
				ezuiTreegrid.treegrid('reload');
				ezuiDialog.dialog('close');
				$.messager.progress('close');
			}
		}
	});
};

var clearParentName = function(){
	$('#parentCombotree').combotree('clear');
};
</script>

</head>
<body>
	<input type="hidden" id="menuId" name="menuId" value="${menuId}"/>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options='region:"center",border:false' style="overflow: hidden;">
			<div id="toolbar" class="datagrid-toolbar" style="padding: 5px;">
				<div>
					<a onclick="add();" 	id="ezuiBtn_add"		class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-add"'		href="javascript:void(0);"><spring:message code="common.button.add"/></a>
					<a onclick="del();" 	id="ezuiBtn_del"	 	class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-remove"'	href="javascript:void(0);"><spring:message code="common.button.delete"/></a>
					<a onclick="edit();" 	id="ezuiBtn_edit"	 	class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-edit"'		href="javascript:void(0);"><spring:message code="common.button.edit"/></a>
					<a onclick="clearTreegridSelected('#ezuiTreegrid');" class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-undo"'	href="javascript:void(0);"><spring:message code="common.button.cancelSelect"/></a>
				</div>
			</div>
			<table id="ezuiTreegrid"></table> 
		</div>
	</div>
	
	<div id="ezuiDialog" style="padding: 10px;">
		<form id="ezuiForm" method="post">
			<input type="hidden" name="menuId"/>
			<table>
				<tr>
					<th>类型</th>
					<td>
						<input type="text" id="menuTypeCombobox" class="easyui-combobox" name="menuType" size='16' data-options="	panelHeight:'auto',
																																	editable:false,
																																	required:true,
																																	valueField: 'id',
																																	textField: 'value',
																																	data: [
																																		{id: '0', value: '目录'},
																																		{id: '1', value: '功能'}
																																	],
																																	onSelect : function(record){
																																		if(record.id == 0){
																																			$('#url').textbox('disable');
																																		}else{
																																			$('#url').textbox('enable');
																																		}
																																	}"/> 
				 	</td>
					<th>名称</th>
					<td><input type="text" name="menuName" class="easyui-textbox" size='16' data-options='required:true'/></td>
					<th>显示顺序</th>
					<td><input type="text" id="displaySeq" name="displaySeq" class="easyui-numberbox" size='16' data-options="required:true,max:100"/></td>
				</tr>
				<tr>
					<th>路径</th>
					<td colspan="5"><input type="text" id="url" name="url" class="easyui-textbox" size='73' data-options='required:true'/></td>
				</tr>
				<tr>
					<th>父节点</th>
					<td colspan="5">
						<input type="text" id="parentCombotree" name="parentId" class="easyui-combotree" size='66' data-options="url:'<c:url value="/menuController.do?showTree"/>',
																																animate:true,
																																lines:true,
																																onSelect:function(node){
																																},
																																onLoadSuccess:function(node, data) {
																																	var t = $(this);
																																	if (data) {
																																		$(data).each(function(index, d) {
																																			if (this.state == 'closed') {
																																				t.tree('expandAll');
																																			}
																																		});
																																	}
																																}">
						<a class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-remove"' onclick="clearParentName();" href="javascript:void(0);"><spring:message code="common.button.clear"/></a>
					</td>
				</tr>
				<tr>
					<th>权限</th>
					<td colspan="5">
						<input type="text" id="roleCombobox" name="roleList" class="easyui-combobox" size='42' style="height:60px" data-options="required:true,
																																				panelHeight:200,
																																				multiple:'multiple',
																																				multiline:true,
																																				editable:false,
																																			    url:'<c:url value="/roleController.do?getCombobox"/>',
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
		<div onclick="del();" id="menu_del"		data-options='plain:true,iconCls:"icon-remove"'><spring:message code="common.button.delete"/></div>
		<div onclick="edit();"id="menu_edit" 	data-options='plain:true,iconCls:"icon-edit"'><spring:message code="common.button.edit"/></div>
	</div> 
</body>
</html>