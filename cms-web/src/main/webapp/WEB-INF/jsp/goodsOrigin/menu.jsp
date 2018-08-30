<%@ page language='java' pageEncoding='UTF-8'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://www.springframework.org/tags' prefix='spring'%>
<!DOCTYPE html>
<html>
<title>虫妈管理系统</title>
	<c:import url='/WEB-INF/jsp/include/bootstrap.jsp' />
</head>
<body>
<header id="header">
	<ul id="menu">
		<li id="guide" class="line-trigger">
			<div class="line-wrap">
				<div class="line top"></div>
				<div class="line center"></div>
				<div class="line bottom"></div>
			</div>
		</li>
		<li id="logo" class="hidden-xs">
			<a href="index.html">
				<img src="resources/images/logo.png"/>
			</a>
			<span id="system_title"></span>
		</li>
		<li class="pull-right">
			<ul class="hi-menu">
				<li class="dropdown">
					<a class="waves-effect waves-light" data-toggle="dropdown" href="javascript:;">
						<i class="him-icon zmdi zmdi-more-vert"></i>
					</a>
					<ul class="dropdown-menu dm-icon pull-right">
						<li class="hidden-xs">
							<a class="waves-effect" data-ma-action="fullscreen" href="javascript:fullPage();"><i class="zmdi zmdi-fullscreen"></i> 全屏模式</a>
						</li>
						<li>
							<a class="waves-effect" onclick="outLogin()" href="javascript:;"><i class="zmdi zmdi-run"></i> 退出登录</a>
						</li>
					</ul>
				</li>
			</ul>
		</li>
	</ul>
</header>
<section id="main">
	<!-- 左侧导航区 -->
	<aside id="sidebar">
		<!-- 个人资料区 -->
		<div class="s-profile">
			<a class="waves-effect waves-light" href="javascript:;">
				<div class="sp-pic">
					<img src="resources/images/avatar.jpg"/>
				</div>
				<div class="sp-info" id='login_info'>
					张恕征，您好！
					<i class="zmdi zmdi-caret-down"></i>
				</div>
			</a>
			<ul class="main-menu">
				<li>
					<a class="waves-effect" href="javascript:;"><i class="zmdi zmdi-account"></i> 个人资料</a>
				</li>
				<li>
					<a class="waves-effect" onclick="outLogin()" href="javascript:;"><i class="zmdi zmdi-run"></i> 退出登录</a>
				</li>
			</ul>
		</div>
		<!-- /个人资料区 -->
		<!-- 菜单区 -->
		<ul class="main-menu" id='load_menu'>
		 </ul>
		<!-- /菜单区 -->
	</aside>
	<!-- /左侧导航区 -->
	<section id="content">
		<div class="content_tab">
			<div class="tab_left">
				<a class="waves-effect waves-light" href="javascript:;"><i class="zmdi zmdi-chevron-left"></i></a>
			</div>
			<div class="tab_right">
				<a class="waves-effect waves-light" href="javascript:;"><i class="zmdi zmdi-chevron-right"></i></a>
			</div>
			<ul id="tabs" class="tabs">
				<li id="tab_home" data-index="home" data-closeable="false" class="cur">
					<a class="waves-effect waves-light">首页</a>
				</li>
			</ul>
		</div>
		<div class="content_main">
			 
		</div>
	</section>
</section>
<footer id="footer"></footer>

<script src='<c:url value="resources/js/admin.js"/>'></script>

<script>
$(function() {
	$.ajax({
		url : "testController.do?getTree",
		type : 'post',
		async : false,
		dataType : 'JSON',
		success : function(data) {
			var _html='';
			for (var i = 0; i < data.length; i++) {
				$('#login_info').html(data[i].loginUserName+"<i class='zmdi zmdi-caret-down'></i>");
				var nodes = data[i].nodes; 
				if (nodes.length > 0) { 
					for (var j = 0; j < nodes.length; j++) {
						_html += "<li class='sub-menu system_menus system_1 0'> <a class='waves-effect' href='javascript:;'><i class='zmdi zmdi-accounts-list'></i> "+nodes[j].text+"</a>";
							var nodes1 = nodes[j].nodes;
							if (nodes1.length > 0) {
								_html += "<ul>";
								for (var k = 0; k < nodes1.length; k++) {
									_html += "<li><a class='waves-effect' href='javascript:Tab.addTab(\""+nodes1[k].text+"\", \""+nodes1[k].href.substring(1,nodes1[k].href.length)+"\");'>"+nodes1[k].text+"</a></li>"
								}
								_html += "</ul><li>";
							}
					}
					_html +="<li><div class='upms-version'>&copy; 测试版本 V0.0.1</div></li>"
				} 
			}
			$('#load_menu').html(_html)
		}
	});
	var systemid =  5;
	var systemname = "zheng-oss-web";
	var systemtitle = "虫妈CMS管理系统";
	$('#system_title').text("虫妈CMS管理系统"); 
})
//退出登录
var outLogin=function(){
	window.top.location.replace('/cms-web/index.html');
}
</script>
</body>
</html>
