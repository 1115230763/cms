<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<c:import url="/WEB-INF/jsp/include/meta.jsp" /> 
<c:import url="/WEB-INF/jsp/include/easyui.jsp" />
<link rel="stylesheet" type="text/css" href='<c:url value="/css/baseStyle.css"/>'/>
<script type="text/javascript">
var rowCount = 0;
var processType;
var ezuiMenu;
var ezuiForm;
var ezuiDialog;
var ezuiDatagrid;
$(function() {
	ezuiMenu = $('#ezuiMenu').menu();
	ezuiForm = $('#ezuiForm').form();
	ezuiDatagrid = $('#ezuiDatagrid').datagrid({
		url : '<c:url value="/roleController.do?showDatagrid"/>',
		method:"POST",
	    title: "角色管理",
	    toolbar : '#toolbar',
		pageSize : 10,
		pageList : [10, 30, 50],
		fit: true,
		border: false,
		fitColumns : true,
	    nowrap: true,  
	    striped: true,  
	    collapsible:false,
	    pagination:false,  
		rownumbers:true, 
		singleSelect:true,
		idField : 'id',
		columns : [[
			{field: 'roleName',		title: '角色名稱',	width: 120 },
			{field: 'btnSet',		title: '可使用按钮',	width: 120, formatter:function(value,rowData,rowIndex){
				var result = "";
				if(value){
					for(var i = 0; i < value.length; i++){
						result = result + value[i].btnChsName;
						if(i != (value.length-1)){
							result = result + ",";
						}
                    }
				}
                   return result;
            }}
	    ]],
	    view: detailview,  
	    detailFormatter: function(rowIndex, rowData){  
	    	return '<div style="padding:2px"><table class="ddv"></table></div>';
	    }, 
	    onExpandRow: function(index,row){
	    	for(var i = 0 ; i < rowCount ; i++){
	    		if(index != i){
	    			$(this).datagrid('collapseRow', i);
	    		}
			}
	    	var dataArray = [];
	    	for(var i = 0 ; i < row.menuList.length ; i++){
	    		if(row.menuList[i].menuType == 1){
	    			dataArray.push(row.menuList[i]);
	    		}
	    	}
	        var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
	        ddv.datagrid({
	        	data : dataArray,
	            fitColumns:true, singleSelect:true,rownumbers:true,height:'auto',
	            columns:[[
	                {field:'menuName',title:'可使用表單',width:100}
	            ]],
	            onResize:function(){
	                $('#ezuiDatagrid').datagrid('fixDetailRowHeight',index);
	            },
	            onLoadSuccess:function(){
	                setTimeout(function(){
	                    $('#ezuiDatagrid').datagrid('fixDetailRowHeight',index);
	                },0);
	            }
	        });
	        $('#ezuiDatagrid').datagrid('fixDetailRowHeight',index);
	    },
		onDblClickCell: function(index,field,value){
			edit();
		},
		onRowContextMenu : function(event, rowIndex, rowData) {
			event.preventDefault();
			$(this).datagrid('unselectAll');
			$(this).datagrid('selectRow', rowIndex);
			ezuiMenu.menu('show', {
				left : event.pageX,
				top : event.pageY
			});
		},onLoadSuccess:function(data){
			rowCount = data.rows.length;
			ajaxBtn($('#menuId').val(), '<c:url value="/roleController.do?getBtn"/>', ezuiMenu);
			$(this).datagrid('unselectAll');
			$('input[name=readonly]').each(function(){
				$(this).click(function(){
					return false;
				});
			});
		}
	});
	
	ezuiDialog = $('#ezuiDialog').show().dialog({
		modal : true,
		title : '详细资料',
		buttons : '#ezuiDialogBtn',
		onClose : function() {
			ezuiFormClear(ezuiForm);
		}
	}).dialog('close');
});

var add = function(){
	processType = 'add';
	ezuiDialog.dialog('open');
};

var edit = function(){
	processType = 'edit';
	var row = ezuiDatagrid.datagrid('getSelected');
	if(row){
		ezuiForm.form('load',{
			roleId : row.id,
			roleName : row.roleName
		});
		var btn = row.btnSet;
		if(btn){
			var btnArray = [];
			for(var i = 0; i < btn.length; i++){
				btnArray[i] = btn[i].id;
		    }
			$('#btnCombobox').combobox('setValues',btnArray);
		}
		
		ezuiDialog.dialog('open');
	}else{
		$.messager.show({
			msg : "<spring:message code='common.message.selectRecord'/>", title : "<spring:message code='common.message.prompt'/>"
		});
	}
};

var del = function(){
	var row = ezuiDatagrid.datagrid('getSelected');
	if(row.roleName == "ROLE_AD"){
		$.messager.show({
			msg : 'ROLE_AD角色無法刪除！', title : "<spring:message code='common.message.prompt'/>"
		});
		return false;
	}
	if(row){
		$.messager.confirm("<spring:message code='common.message.confirm'/>", "<spring:message code='common.message.confirm.delete'/>", function(confirm) {
			if(confirm){
				$.ajax({
					url : 'roleController.do?delete',
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
							ezuiDatagrid.datagrid('reload');
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
		url = '<c:url value="/roleController.do?edit"/>';
	}else{
		url = '<c:url value="/roleController.do?add"/>';
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
			var msg = "";
			try {
				msg = $.parseJSON(data).msg;
			} catch (e) {
				msg = "<spring:message code='common.message.data.process.failed'/><br/><br/>"+ data;
			} finally {
				$.messager.show({
					msg : msg, title : "<spring:message code='common.message.prompt'/>"
				});
				ezuiDatagrid.datagrid('reload');
				ezuiDialog.dialog('close');
				$.messager.progress('close');
			}
		}
	});
};
</script>

</head>
<body>
	<input type="hidden" id="menuId" name="menuId" value="${menuId}"/>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options='region:"center",border:false' style="overflow: hidden;">
			<div id="toolbar" class="datagrid-toolbar" style="padding: 5px;">
				<div>
					<a onclick="add();" 	id="ezuiBtn_add"		class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-add"'	href="javascript:void(0);"><spring:message code="common.button.add"/></a>
					<a onclick="del();" 	id="ezuiBtn_del"	 	class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-remove"'	href="javascript:void(0);"><spring:message code="common.button.delete"/></a>
					<a onclick="edit();" 	id="ezuiBtn_edit"	 	class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-edit"'	href="javascript:void(0);"><spring:message code="common.button.edit"/></a>
					<a onclick="clearDatagridSelected('#ezuiDatagrid');" class="easyui-linkbutton" data-options='plain:true,iconCls:"icon-undo"'	href="javascript:void(0);"><spring:message code="common.button.cancelSelect"/></a>
				</div>
			</div>
			<table id="ezuiDatagrid"></table> 
		</div>
	</div>
	
	<div id="ezuiDialog" style="padding: 10px;">
		<form id="ezuiForm" method="post">
			<input type="hidden" name="roleId"/>
			<table>
				<tr>
					<th nowrap="nowrap">角色名稱</th>
					<td><input type="text" name="roleName" class="easyui-textbox" size='16' data-options='required:true'/></td>
				</tr>
				<tr>
					<th nowrap="nowrap">可使用按钮</th>
					<td><input type="text" id="btnCombobox" name="btns" class="easyui-combobox" size='16' style='height:80px' data-options="panelHeight:'auto',
									 																										multiple:'multiple',
									 																										multiline:true,
									 																										editable:false,
									 																									    url:'<c:url value="/btnController.do?getCombobox"/>',
									 																									    valueField:'id',  
									 																									    textField:'value'
																																		 " />
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
		<div onclick="add();" id="menu_add"	data-options='plain:true,iconCls:"icon-add"'><spring:message code="common.button.add"/></div>
		<div onclick="del();" id="menu_del"	data-options='plain:true,iconCls:"icon-remove"'><spring:message code="common.button.delete"/></div>
		<div onclick="edit();"id="menu_edit" 	data-options='plain:true,iconCls:"icon-edit"'><spring:message code="common.button.edit"/></div>
	</div> 
</body>
</html>