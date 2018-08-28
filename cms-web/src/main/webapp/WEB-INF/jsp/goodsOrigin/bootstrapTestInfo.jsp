<%@ page language='java' pageEncoding='UTF-8'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://www.springframework.org/tags' prefix='spring'%>
<!DOCTYPE html>
<html>
<head> 


<script charset="UTF-8" type="text/javascript" src="<c:url value="/js/jquery/jquery.min.js"/>"></script>
<script charset="UTF-8" type="text/javascript" src="<c:url value="/js/toastr.min.js"/>"></script>
<script charset="UTF-8" type="text/javascript" src="<c:url value="/js/Ewin.js"/>"></script>
<script charset="UTF-8" type="text/javascript" src="<c:url value="/js/bootstrap/js/bootstrap.min.js"/>"></script>
<script charset="UTF-8" type="text/javascript" src="<c:url value="/js/bootstrap/js/bootstrap-table.js"/>"></script>
<script charset="UTF-8" type="text/javascript" src="<c:url value="/js/bootstrap/js/bootstrap-table-zh-CN.js"/>"></script>




<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/css/toastr.min.css"/>'>
<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/js/bootstrap/css/bootstrap.min.css"/>'>
<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/js/bootstrap/css/bootstrap-table.css"/>'>


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
<form class="form-horizontal" role="form">
<div class="container" >
   <div class="form-group">
      <label for="firstname" class="col-sm-2 control-label">商品名称：</label>
      <div class="col-sm-6">
         <input type="text" class="form-control" id="firstname" 
            placeholder="请输入">
      </div> 
      <label for="lastname" class="col-sm-2 control-label">商品品类：</label>
      <div class="col-sm-2">
         <input type="text" class="form-control" id="lastname" 
            placeholder="请输入">
      </div>
   </div>
   <div class="form-group">
      <label for="firstname" class="col-sm-2 control-label">产地：</label>
      <div class="col-sm-2">
         <input type="text" class="form-control" id="firstname" 
            placeholder="请输入">
      </div> 
      <label for="lastname" class="col-sm-2 control-label">规格：</label>
      <div class="col-sm-2">
         <input type="text" class="form-control" id="lastname" 
            placeholder="请输入">
      </div>
   </div>
   <div class="form-group">
      <label for="firstname" class="col-sm-2 control-label">采购类型：</label>
      <div class="col-sm-2">
         <input type="text" class="form-control" id="firstname" 
            placeholder="请输入">
      </div> 
      <label for="lastname" class="col-sm-2 control-label">存储方式：</label>
      <div class="col-sm-2">
         <input type="text" class="form-control" id="lastname" 
            placeholder="请输入">
      </div>
      <label for="lastname" class="col-sm-2 control-label">存储类型：</label>
      <div class="col-sm-2">
         <input type="text" class="form-control" id="lastname" 
            placeholder="请输入">
      </div>
   </div>
   <div class="form-group">
      <label for="firstname" class="col-sm-2 control-label">状态：</label>
      <div class="col-sm-2">
           <label class="radio-inline">
		        <input type="radio" name="optionsRadiosinline" id="optionsRadios3" value="option1" checked> 有效
		    </label>
		    <label class="radio-inline">
		        <input type="radio" name="optionsRadiosinline" id="optionsRadios4"  value="option2"> 无效
		    </label>
      </div>
   </div>
   <div class="form-group">
      <label for="firstname" class="col-sm-2 control-label">缩减图：</label>
      <div class="col-sm-6">
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
   <div class="form-group">
      <label for="firstname" class="col-sm-2 control-label">商品描述：</label>
      <div class="col-sm-6">
         <textarea class="form-control" rows="3" name=textarea></textarea>
      </div>
   </div>
    <div class="form-group">
      <label for="firstname" class="col-sm-2 control-label">保质期：</label>
      <div class="col-sm-2">
         <input type="text" class="form-control" id="firstname" 
            placeholder="请输入">
      </div>
      <div class="col-sm-1">
         <input type="text" class="form-control" id="firstname" 
            placeholder="请输入">
      </div>
   </div>
</div>
</form>
</body>
</html>