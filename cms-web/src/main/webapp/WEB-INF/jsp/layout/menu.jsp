<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<style>
	.header_logo{
		width: 210px;
		height: 120px;
		padding-top: 10px;
		border-radius: 4px;
		vertical-align: top;
		display: inline-block;
		border-bottom: 1px solid #D9D9D9;
		border-right: 1px solid #D9D9D9;
		background:url(<c:url value="/images/menuImg/logo.png"/>) no-repeat center center;
	}
</style>
<script type="text/javascript" charset="UTF-8">
$(function(){
	if('${sessionScope.userInfo}'){
		getMenu();
	}
});

</script>
<div class="easyui-layout" data-options='fit:true'>
	<div data-options='region:"center",border:false'>
		<div class="header_logo"></div>
		<ul id="menu" style="margin-top: 5px;"></ul>
	</div>
</div>