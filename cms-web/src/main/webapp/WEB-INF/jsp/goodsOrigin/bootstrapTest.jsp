<%@ page language='java' pageEncoding='UTF-8'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://www.springframework.org/tags' prefix='spring'%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>通用增删改查</title>

	<link href="resources/plugins/bootstrap-3.3.0/css/bootstrap.min.css" rel="stylesheet"/>
	<link href="resources/plugins/material-design-iconic-font-2.2.0/css/material-design-iconic-font.min.css" rel="stylesheet"/>
	<link href="resources/plugins/bootstrap-table-1.11.0/bootstrap-table.min.css" rel="stylesheet"/>
	<link href="resources/plugins/waves-0.7.5/waves.min.css" rel="stylesheet"/>
	<link href="resources/plugins/jquery-confirm/jquery-confirm.min.css" rel="stylesheet"/>
	<link href="resources/plugins/select2/css/select2.min.css" rel="stylesheet"/>

	<link href="resources/css/common.css" rel="stylesheet"/>
	
<style type="text/css">
	*{ font-family:"宋体"}
	.clearFile {
        position: relative;
        display: block;
        text-indent: 0;
        float: left;
        margin-top: 70px;
        margin-left: 20px;
        border-radius: 5px;
        width: 80px;
        color: #ffffff;
        background: #1b94ff;
        text-decoration: none;
        line-height: 30px;
        text-align: center;
        height: 30px;
    }

    .clearFile:hover {
        text-decoration: none;
        background: #6a96df;
        color: white;
    }

    .file {
        position: relative;
        display: block;
        text-indent: 0;
        float: left;
        margin-top: 70px;
        margin-left: 20px;
        border-radius: 5px;
        width: 80px;
        color: #ffffff;
        background: #1b94ff;
        text-decoration: none;
        line-height: 30px;
        text-align: center;
        height: 30px;
    }

    .file:hover {
        text-decoration: none;
        background: #6a96df;
        color: white;
    }

    .file input {
        position: absolute;
        width: 183px;
        height: 48px;
        right: 0px;
        top: 0;
        opacity: 0;
    }

    .sfzimg {
        width: 100px;
        height: 100px;
        float: left;
        padding: 1px;
        display: block;
        border-radius: 5px;
        border: 2px solid #48aada;
    }
</style>
</head>
<body>
<div id="main">
	<div id="toolbar">
		<a class="waves-effect waves-button" href="javascript:;" onclick="createAction()"><i class="zmdi zmdi-plus"></i> 新增用户</a>
		<a class="waves-effect waves-button" href="javascript:;" onclick="updateAction()"><i class="zmdi zmdi-edit"></i> 编辑用户</a>
		<a class="waves-effect waves-button" href="javascript:;" onclick="deleteAction()"><i class="zmdi zmdi-close"></i> 删除用户</a>
	</div>
	<table id="table"></table>
</div>
<!-- 新增 -->
<div id="createDialog" class="crudDialog"   hidden>
	<form>
	<div class="container" >
	<div class="form-group col-sm-8">
      <label for="firstname" >商品名称：</label>
         <input type="text" class="form-control" id="firstname">
    </div>
    <div class="form-group col-sm-4">
      <label for="lastname" >商品品类：</label>
         <input type="text" class="form-control" id="lastname">
   </div>
   <div class="form-group col-sm-4">
      <label for="firstname">产地：</label>
         <input type="text" class="form-control" id="firstname" >
    </div>
    <div class="form-group col-sm-4">
      <label for="lastname" >规格：</label>
         <input type="text" class="form-control" id="lastname" > 
   </div>
   <div class="form-group col-sm-4">
      <label for="firstname col-sm-4" >采购类型：</label>
         <input type="text" class="form-control" id="firstname">
   </div>
    <div class="form-group col-sm-4">
      <label for="lastname" >存储方式：</label>
         <input type="text" class="form-control" id="lastname" > 
    </div>
    <div class="form-group col-sm-4">
      <label for="lastname" >存储类型：</label>
         <input type="text" class="form-control" id="lastname" > 
   </div> 
   <div class="form-group col-sm-12" style='padding: 0px;'>
   <div class='col-sm-1'><label>缩减图:</label></div>
   <div class='col-sm-11'>
         <div id="imgFile" class="sfzimg"></div>
			<div class="images"> 
				<a href="javascript:;" class="file">选择图片
				<input type="file" name="goodsImageFile" id="fileImg"
					onchange="upload('imgFile','goodsImage','goodsOriginController.do?upload')">
					<input type="hidden" id="goodsImage" name="goodsImage"/>
				</a>
			</div>
			<a href="javascript:;" class='clearFile' onclick="clearFile('imgFile','goodsImage')" >清空图片</a> 
	</div>
   </div>
   <div class="form-group col-sm-12">
      <label for="firstname" >商品描述：</label>
      <input type="text" class="form-control" id="firstname"> 
   </div>
    <div class="form-group col-sm-12" style='padding: 0px;'>
       <div class='col-sm-2'><label for="firstname" >保质期：</label></div>
      <div class='col-sm-2'><label for="firstname" >天数</label><input type="text" class="form-control" id="firstname"> </div>
      <div class='col-sm-2'><label for="firstname" >单位</label><select><option value='天'>天</option><option value='年'>月</option><option value='月'>年</option></select></div>
   </div>
   </div>
	</form>
</div>
<script src="resources/plugins/jquery.1.12.4.min.js"></script>
<script src="resources/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>
<script src="resources/plugins/bootstrap-table-1.11.0/bootstrap-table.min.js"></script>
<script src="resources/plugins/bootstrap-table-1.11.0/locale/bootstrap-table-zh-CN.min.js"></script>
<script src="resources/plugins/waves-0.7.5/waves.min.js"></script>
<script src="resources/plugins/jquery-confirm/jquery-confirm.min.js"></script>
<script src="resources/plugins/select2/js/select2.min.js"></script>

<script src="resources/js/common.js"></script>
<script>
var $table = $('#table');
$(function() {
	$(document).on('focus', 'input[type="text"]', function() {
		$(this).parent().find('label').addClass('active');
	}).on('blur', 'input[type="text"]', function() {
		if ($(this).val() == '') {
			$(this).parent().find('label').removeClass('active');
		}
	});
 
	// bootstrap table初始化
	// http://bootstrap-table.wenzhixin.net.cn/zh-cn/documentation/
	$table.bootstrapTable({
		url: '<c:url value="/goodsOriginController.do?showDatagrid"/>',
		height: getHeight(),
		striped: true,
		search: true,
		searchOnEnterKey: true,
		showRefresh: true,
		showToggle: true,
		showColumns: true,
		minimumCountColumns: 2,
		showPaginationSwitch: true,
		clickToSelect: true,
		detailView: true,
		detailFormatter: 'detailFormatter',
		pagination: true,
		paginationLoop: false,
		classes: 'table table-hover table-no-bordered',
		sidePagination: 'server',
		silentSort: false,
		smartDisplay: false,
		idField: 'id',
		sortName: 'id',
		sortOrder: 'desc',
		escape: true,
		searchOnEnterKey: true,
		idField: 'systemId',
		maintainSelected: true,
		toolbar: '#toolbar',
		columns: [
			{checkbox: true,}, 
          			{field: 'goodsId',		title: '商品编码',	  halign: 'center',align: 'center' },
          			{field: 'goodsName',		title: '商品名称',	  halign: 'center',align: 'center' },
          			{field: 'name',		title: '商品品类',   halign: 'center',align: 'center' },
          			{field: 'goodsImage',		title: '图片',  halign: 'center',align: 'center',
          				formatter:function(value,rowData,rowIndex){
          					if(value != ""){
          						return " ";
          					}else {
          						return "";
          					}
          					}},
          			{field: 'goodsStandard',		title: '原子规格',	  halign: 'center',align: 'center' },
          			{field: 'value',		title: '存储类型',	  halign: 'center',align: 'center' },
          			{field: 'purchaseTypeName',		title: '采购类型',	 halign: 'center',align: 'center' },
          			{field: 'status',		title: '状态',	  halign: 'center',align: 'center',
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
                        },
			{field: 'action', title: '操作', halign: 'center', align: 'center', formatter: 'actionFormatter', events: 'actionEvents', clickToSelect: false}
		]
	}).on('all.bs.table', function (e, name, args) {
		$('[data-toggle="tooltip"]').tooltip();
		$('[data-toggle="popover"]').popover();  
	});
}); 
 






function actionFormatter(value, row, index) {
    return [
        '<a class="like" href="javascript:void(0)" data-toggle="tooltip" title="Like"><i class="glyphicon glyphicon-heart"></i></a>　',
        '<a class="edit ml10" href="javascript:void(0)" data-toggle="tooltip" title="Edit"><i class="glyphicon glyphicon-edit"></i></a>　',
        '<a class="remove ml10" href="javascript:void(0)" data-toggle="tooltip" title="Remove"><i class="glyphicon glyphicon-remove"></i></a>'
    ].join('');
}

window.actionEvents = {
    'click .like': function (e, value, row, index) {
        alert('You click like icon, row: ' + JSON.stringify(row));
        console.log(value, row, index);
    },
    'click .edit': function (e, value, row, index) {
        alert('You click edit icon, row: ' + JSON.stringify(row));
        console.log(value, row, index);
    },
    'click .remove': function (e, value, row, index) {
        alert('You click remove icon, row: ' + JSON.stringify(row));
        console.log(value, row, index);
    }
};
function detailFormatter(index, row) {
	var html = [];
	$.each(row, function (key, value) {
		html.push('<p><b>' + key + ':</b> ' + value + '</p>');
	});
	return html.join('');
}
// 新增
function createAction() {
	$.confirm({
		type: 'dark',
		animationSpeed: 800,
		columnClass:"col-md-12",
		title: '新增原子商品',
		content: $('#createDialog').html(),
		buttons: {
			confirm: {
				text: '确认',
				btnClass: 'waves-effect waves-button',
				action: function () {
					$.alert('确认');
				}
			},
			cancel: {
				text: '取消',
				btnClass: 'waves-effect waves-button'
			}
		}
	});
}
// 编辑
function updateAction() {
	var rows = $table.bootstrapTable('getSelections');
	if (rows.length == 0) {
		$.confirm({
			title: false,
			content: '请至少选择一条记录！',
			autoClose: 'cancel|3000',
			backgroundDismiss: true,
			buttons: {
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	} else {
		$.confirm({
			type: 'blue',
			animationSpeed: 300,
			title: '编辑系统',
			content: $('#createDialog').html(),
			buttons: {
				confirm: {
					text: '确认',
					btnClass: 'waves-effect waves-button',
					action: function () {
						$.alert('确认');
					}
				},
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	}
}
// 删除
function deleteAction() {
	var rows = $table.bootstrapTable('getSelections');
	if (rows.length == 0) {
		$.confirm({
			title: false,
			content: '请至少选择一条记录！',
			autoClose: 'cancel|3000',
			backgroundDismiss: true,
			buttons: {
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	} else {
		$.confirm({
			type: 'red',
			animationSpeed: 300,
			title: false,
			content: '确认删除该系统吗？',
			buttons: {
				confirm: {
					text: '确认',
					btnClass: 'waves-effect waves-button',
					action: function () {
						var ids = new Array();
						for (var i in rows) {
							ids.push(rows[i].systemId);
						}
						$.alert('删除：id=' + ids.join("-"));
					}
				},
				cancel: {
					text: '取消',
					btnClass: 'waves-effect waves-button'
				}
			}
		});
	}
}
</script>
</body>
</html>