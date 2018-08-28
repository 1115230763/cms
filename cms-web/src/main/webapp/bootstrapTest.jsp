<%@ page language='java' pageEncoding='UTF-8'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://www.springframework.org/tags' prefix='spring'%>
<!DOCTYPE html>
<html>
<head> 


<script charset="UTF-8" type="text/javascript" src="<c:url value="/js/jquery/jquery.min.js"/>"></script>
<script charset="UTF-8" type="text/javascript" src="<c:url value="/js/toastr.min.js"/>"></script>
<script charset="UTF-8" type="text/javascript" src="<c:url value="/js/bootstrap/js/bootstrap.min.js"/>"></script>
<script charset="UTF-8" type="text/javascript" src="<c:url value="/js/bootstrap/js/bootstrap-table.js"/>"></script>
<script charset="UTF-8" type="text/javascript" src="<c:url value="/js/bootstrap/js/bootstrap-table-zh-CN.js"/>"></script>




<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/css/toastr.min.css"/>'>
<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/js/bootstrap/css/bootstrap.min.css"/>'>
<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/js/bootstrap/css/bootstrap-table.css"/>'>
 


<script type="text/javascript" src="js/Ewin.js"></script>
</head>
<script type="text/javascript">


$(function () {
    //1.初始化Table
    var oTable = new TableInit();
    oTable.Init();

    //2.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();
});
var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_departments').bootstrapTable({
            url: '<c:url value="/waveController.do?showDatagrid"/>',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            //showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{checkbox: true,}, 
                      {field: 'enterinfoid',visible:false,}, 
                      {field: 'waveNo',title: '波次单号',align:'center',halign:'center'}, 
                      {field: 'typeName',title: '波次类型',align:'left',halign:'center'},
                      {field: 'areaName',title: '片区',align:'left',halign:'center'},
                      {field: 'value',title: '温区',align:'center',halign:'center'},
                      {field: 'batchName',title: '批次名称',align:'left',halign:'center'},
                      {field: 'num',title: '出库单数量',align:'right',halign:'center'},
                      {field: 'quantityExpected',title: '商品总数量',align:'right',halign:'center'},
                      {field: 'status',title: '波次状态',align:'center',halign:'left',
                    	  formatter:function(value,rowData,rowIndex){
	      					if(value!=null){
	    			        	return value.split('_')[0];
	    			        }else{
	    			        	return '';
	    			        }
      					},},
                      {field: 'createBy',title: '创建人',align:'left',halign:'center'},
                      {field: 'createDate',title: '创建时间',align:'center',halign:'center',
                    	  formatter:function(value,rowData,rowIndex){ 
          					if(value!='' && value!=null){
                      			return value.substr(0,10);
                      		}else{
                      			return '';
                      		} 
                      	}},
                      ]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            departmentname: $("#txt_search_departmentname").val(),
            statu: $("#txt_search_statu").val()
        };
        return temp;
    };
    return oTableInit;
};


var ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
    	$("#btn_add").click(function () {
            $("#myModalLabel").text("新增");
            $("#myModal").find(".form-control").val("");
            $('#myModal').modal()

            postdata.DEPARTMENT_ID = "";
        });

        $("#btn_edit").click(function () {
            var arrselections = $("#tb_departments").bootstrapTable('getSelections');
            if (arrselections.length > 1) {
                toastr.warning('只能选择一行进行编辑');
                return;
            }
            if (arrselections.length <= 0) {
                toastr.warning('请选择有效数据');

                return;
            }
            $("#myModalLabel").text("编辑");
            $("#txt_departmentname").val(arrselections[0].DEPARTMENT_NAME);
            $("#txt_parentdepartment").val(arrselections[0].PARENT_ID);
            $("#txt_departmentlevel").val(arrselections[0].DEPARTMENT_LEVEL);
            $("#txt_statu").val(arrselections[0].STATUS);

            postdata.DEPARTMENT_ID = arrselections[0].DEPARTMENT_ID;
            $('#myModal').modal();
        });

        $("#btn_delete").click(function () {
            var arrselections = $("#tb_departments").bootstrapTable('getSelections');
            if (arrselections.length <= 0) {
                toastr.warning('请选择有效数据');
                return;
            }

            Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
                if (!e) {
                    return;
                }
                $.ajax({
                    type: "post",
                    url: "/Home/Delete",
                    data: { "": JSON.stringify(arrselections) },
                    success: function (data, status) {
                        if (status == "success") {
                            toastr.success('提交数据成功');
                            $("#tb_departments").bootstrapTable('refresh');
                        }
                    },
                    error: function () {
                        toastr.error('Error');
                    },
                    complete: function () {

                    }

                });
            });
        });

        $("#btn_submit").click(function () {
            postdata.DEPARTMENT_NAME = $("#txt_departmentname").val();
            postdata.PARENT_ID = $("#txt_parentdepartment").val();
            postdata.DEPARTMENT_LEVEL = $("#txt_departmentlevel").val();
            postdata.STATUS = $("#txt_statu").val();
            $.ajax({
                type: "post",
                url: "/Home/GetEdit",
                data: { "": JSON.stringify(postdata) },
                success: function (data, status) {
                    if (status == "success") {
                        toastr.success('提交数据成功');
                        $("#tb_departments").bootstrapTable('refresh');
                    }
                },
                error: function () {
                    toastr.error('Error');
                },
                complete: function () {

                }

            });
        });

        $("#btn_query").click(function () {
            $("#tb_departments").bootstrapTable('refresh');
        });
    };

    return oInit;
};
</script>

<body> 

<!-- 模态框 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
	aria-labelledby="gridSystemModalLabel" data-backdrop="static">
	<div class="modal-dialog" role="document" id="modalDialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close" onclick="removeFrom()">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
					
				</h4>
			</div>
			<div class="modal-body" id="commonModalbody">
				
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"  onclick="removeFrom()">
					关闭
				</button>
				<button  type="button" class="btn btn-primary" id="baocun" onclick="fromSubmit()">
					保存
				</button>
			</div>
		</div>
	</div>
</div>


 <div class="panel-body" style="padding-bottom:0px;">
        <div class="panel panel-default">
            <div class="panel-heading">查询条件</div>
            <div class="panel-body">
                <form id="formSearch" class="form-horizontal">
                    <div class="form-group" style="margin-top:15px">
                        <label class="control-label col-sm-1" for="txt_search_departmentname">部门名称</label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" id="txt_search_departmentname">
                        </div>
                        <label class="control-label col-sm-1" for="txt_search_statu">状态</label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" id="txt_search_statu">
                        </div>
                        <div class="col-sm-4" style="text-align:left;">
                            <button type="button" style="margin-left:50px" id="btn_query" class="btn btn-primary">查询</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>       

        <div id="toolbar" class="btn-group">
            <button id="btn_add" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
            </button>
            <button id="btn_edit" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改
            </button>
            <button id="btn_delete" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
            </button>
        </div>
        <table id="tb_departments"></table>
    </div>
</body>
</html>