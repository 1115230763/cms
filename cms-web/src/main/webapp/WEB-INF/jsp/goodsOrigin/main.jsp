<%@ page language='java' pageEncoding='UTF-8'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://www.springframework.org/tags' prefix='spring'%>
<!DOCTYPE html>
<html>
<head>
<c:import url='/WEB-INF/jsp/include/meta.jsp' />
<c:import url='/WEB-INF/jsp/include/easyui.jsp' />
<c:import url='/WEB-INF/jsp/goodsOrigin/goodsOriginInfo.jsp'/> 
<link rel="stylesheet" type="text/css" href='<c:url value="/css/baseStyle.css"/>'/>
<script type='text/javascript'>
var processType;
var ezuiMenu;
var ezuiForm;
var ezuiDialog;
var ezuiDatagrid;
$(function() {
	ezuiMenu = $('#ezuiMenu').menu();
	ezuiForm = $('#ezuiGoodsOriginInfoForm').form();
	ezuiDatagrid = $('#ezuiDatagrid').datagrid({
		url : '<c:url value="/goodsOriginController.do?showDatagrid"/>',
		method:'POST',
		toolbar : '#toolbar',
		pageSize : 50,
		pageList : [50, 100, 200],
		fit: true,
		border: false,
		fitColumns : true,
		nowrap: true,
		striped: true,
		collapsible:false,
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		idField : 'id',
		columns : [[
			{field: 'goodsType',   hidden:true,},
			{field: 'goodsOrigin',   hidden:true,},
			{field: 'goodsStorage',   hidden:true,},
			{field: 'purchaseType',   hidden:true,},
			{field: 'goodsId',		title: '商品编码',	width: 20 , halign: 'center',align: 'center' },
			{field: 'goodsName',		title: '商品名称',	width: 57 , halign: 'center',align: 'left' },
			{field: 'name',		title: '商品品类',	width: 20 , halign: 'center',align: 'left' },
			{field: 'goodsImage',		title: '图片',	width: 20 , halign: 'center',align: 'center',
				formatter:function(value,rowData,rowIndex){
					if(value != ""){
						return "<img width='60' height='60' src='"+value+"'>";
					}else {
						return "";
					}
					}},
			{field: 'goodsStandard',		title: '原子规格',	width: 20 , halign: 'center',align: 'left' },
			{field: 'value',		title: '存储类型',	width: 20 , halign: 'center',align: 'left' },
			{field: 'purchaseTypeName',		title: '采购类型',	width: 20 , halign: 'center',align: 'left' },
			{field: 'status',		title: '状态',	width: 10 , halign: 'center',align: 'center',
				formatter:function(value,rowData,rowIndex){
					if(value == 0){
						return "有效";
					}else if(value == 1){
						return "无效";
					}else if(value == 9){
						return "待启用";
					}else{
						return "";
					}
				}
              }
		]],
		onDblClickCell: function(index,field,value){
			edit();
		},onLoadSuccess:function(data){
			$(this).datagrid('unselectAll');
			ezuiDatagrid.datagrid('scrollTo',0);
		}
	});
	ezuiDialog = $('#ezuiGoodsOriginInfoDialog').dialog({
		modal : true,
		title : '<spring:message code="common.dialog.title"/>',
		onClose : function() {
			ezuiFormClear(ezuiForm);
			ezuiDatagrid.datagrid('scrollTo',0);
		}
	}).dialog('close');
});
var edit = function(){
	processType = 'edit';
	var row = ezuiDatagrid.datagrid('getSelected');
	if(row){
		$('#expiryDateUnit').combobox('setValue','天')
		if(row.goodsImage!=''){
			$('.sfzimg').html("<img width='100' height='100' src="+row.goodsImage+">");
		}else{
			$('.sfzimg').html("");
		}
		ezuiForm.form('load',{
			goodsId : row.goodsId,
			goodsName : row.goodsName,
			goodsType : row.goodsType,
			goodsOrigin : row.goodsOrigin,
			goodsStandard : row.goodsStandard,
			purchaseType : row.purchaseType,
			goodsStorage : row.goodsStorage,
			storageType : row.storageType,
			goodsImage : row.goodsImage,
			goodsDesc : row.goodsDesc,
			expiryDate : row.expiryDate,
			status : row.status,
			createDate : row.createDate,
			updateDate : row.updateDate
		});
		ezuiDialog.dialog('open');
	}else{
		$.messager.show({
			msg : '<spring:message code="common.message.selectRecord"/>', title : '<spring:message code="common.message.prompt"/>'
		});
	}
};
var doSearch = function(){
	ezuiDatagrid.datagrid('load', {
		goodsId : $('#goodsId').val(),
		goodsName : $('#goodsName').val(),
		goodsType : $('#goodsType').combobox('getValue'),
		status : $('#status').combobox('getValue')
	});
};
 


</script> 
</head>
<body>
	<input type='hidden' id='menuId' name='menuId' value='${menuId}'/>
	<div class='easyui-layout' data-options='fit:true,border:false'>
		<div data-options='region:"center",border:false' style='overflow: hidden;'>
			<div id='toolbar' class='datagrid-toolbar' style='padding: 5px;'>
				<div>
					<a onclick='edit();' id='ezuiBtn_edit' class='easyui-linkbutton' data-options='plain:true,iconCls:"icon-edit"' href='javascript:void(0);'><spring:message code='common.button.edit'/></a>
				</div>
				<fieldset>
					<legend><spring:message code='common.legend.query'/></legend>
					<table>
						<tr>
							<th>商品品类：</th><td><input type='text' id='goodsType' class='easyui-combobox' size='16'  data-options="
									panelHeight:'auto',
									panelMaxHeight:200,
									editable:false, 
									valueField:'id',
									textField:'value',
									url:'<c:url value="goodsOriginController.do?getGoodsOriginTypeCombobox"/>', 
									"/></td>
							<th>商品编码：</th><td><input type='text' id='goodsId' class='easyui-numberbox' size='16' data-options=''/></td>
							<th>商品名称：</th><td><input type='text' id='goodsName' class='easyui-textbox' size='16' data-options=''/></td>
							<th>商品状态：</th><td><input type='text' id='status' class='easyui-combobox' size='16' data-options="
									panelHeight:'auto',
									panelMaxHeight:200,
									editable:false,
									valueField: 'id',
									textField: 'value',
									data: [
										{id: '0', value: '有效' },
										{id: '1', value: '无效' } 
									]"/></td> 
							<td>
								<a onclick='doSearch();' class='easyui-linkbutton' data-options='plain:true,iconCls:"icon-search"' href='javascript:void(0);'>查詢</a>
								<a onclick='ezuiToolbarClear("#toolbar");' class='easyui-linkbutton' data-options='plain:true,iconCls:"icon-remove"' href='javascript:void(0);'><spring:message code='common.button.clear'/></a>
							</td>
						</tr>
					</table>
				</fieldset> 
			</div>
			<table id='ezuiDatagrid'></table> 
		</div>
	</div> 
</body>
</html>
