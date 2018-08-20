/**
 * 包含easyui的扩展和常用的方法
 */
var sy = $.extend({}, sy);/* 全域变数 */

(function () { // 修改messager.show 全局timeout的显示时间
    var _showFun = $.messager.show;
    $.messager.show = function () {
        if (_showFun) {
            if (!arguments[0].timeout) {
                arguments[0].timeout = 5000;
            }
            _showFun.apply(this, arguments);
        }
    }
}());

$.ajaxSetup({
	type : 'POST',
	error : function(XMLHttpRequest, textStatus, errorThrown) {/* 擴展AJAX出现錯誤的提示 */
		$.messager.progress('close');
		if(XMLHttpRequest.responseText.indexOf('登入') == -1){
			$.messager.alert({
				title: '系统发生错误',
				msg: "<h3><font color='red'>很抱歉造成您的不便，请将此错误信息截图给客服，我们将尽快修复问题！</font></h3>" + XMLHttpRequest.responseText.indexOf('root cause') > -1 ? XMLHttpRequest.responseText.split('root cause')[1] : XMLHttpRequest.responseText,
				icon: 'error',
				height: 500,
				width: 1060
			});
		}else{
			window.top.showLogoutMsgDialog("帐号已在其他地方登入！");
		}
	}
});

var easyuiErrorFunction = function(XMLHttpRequest) {
	$.messager.progress('close');
	if(XMLHttpRequest.responseText.indexOf('登入') == -1){
		$.messager.alert({
			title: '系统发生错误',
			msg: "<h3><font color='red'>很抱歉造成您的不便，请将此错误信息截图给客服，我们将尽快修复问题！</font></h3>" + XMLHttpRequest.responseText.indexOf('root cause') > -1 ? XMLHttpRequest.responseText.split('root cause')[1] : XMLHttpRequest.responseText,
			icon: 'error',
			height: 500,
			width: 1060
		});
	}else{
		window.top.showLogoutMsgDialog("帐号已在其他地方登入！");
	}
};

var easyuiPanelOnMove = function(left, top) {//防止panel/window/dialog超出瀏覽器邊界
	var l = left;
	var t = top;
	if (l < 1) {
		l = 1;
	}
	if (t < 1) {
		t = 1;
	}
	var width = parseInt($(this).parent().css('width')) + 14;
	var height = parseInt($(this).parent().css('height')) + 14;
	var right = l + width;
	var buttom = t + height;
	var browserWidth = $(window).width();
	var browserHeight = $(window).height();
	if (right > browserWidth) {
		l = browserWidth - width;
	}
	if (buttom > browserHeight) {
		t = browserHeight - height;
	}
	$(this).parent().css({/* 修正面板位置 */
		left : l,
		top : t
	});
};

$.fn.panel.defaults.loadingMessage = '资料加载中，请稍后....';
$.fn.datagrid.defaults.loadMsg = '资料加载中，请稍后....';
$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
$.fn.form.defaults.onLoadError = easyuiErrorFunction;
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;

$.extend($.fn.tree.methods,{ // 增加Tree取消選擇的方法
    unSelect:function(jq,target){
        return jq.each(function(){
            $(target).removeClass("tree-node-selected");
        });
    }
});

$.extend($.fn.validatebox.defaults.rules, {
	isEmail: {
        validator: function(value, param){
        	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/igm;
        	return re.test(value);
        },
        message: '请输入有效电子邮件'
	},
	eqPassword : {/* 驗證密碼輸入是否相同  */
		validator : function(value, param) {
			return value == $(param[0]).val();
		},
		message : '[新密码]与[新密码确认]输入不一致！'
	},
	eng : {
		validator : function(value, param){
			if(value.length <= eval(param[0])){
				return new RegExp("^[a-zA-z]*$").test(value);
			}else{
				return false;
			}
		},
		message : '只能输入{0}个英文字！'
	},
	mobile :{
		validator : function(value, param){
			var re = /^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
			return re.test(value); 
		},
		message : '请输入有效的手机号'
	},
	chinese : {
		validator : function(value, param){
			if(value.length <= eval(param[0])){
				return new RegExp("^[\u4E00-\u9FA5]*$").test(value);
			}else{
				return false;
			}
		},
		message : '只能输入{0}个中文字！'
	},
	engNum : {
		validator : function(value, param){
			if(value.length <= eval(param[0])){
				return new RegExp("^[a-zA-z0-9]*$").test(value);
			}else{
				return false;
			}
		},
		message : '只能输入{0}个英数字！'
	},
	numberOnly : {
		validator : function(value, param){
			return new RegExp("^[0-9]*$").test(value);
		},
		message : '只能输入数字！'
	},
	length : {
		validator : function(value, param){
			if(value.length <= eval(param[0])){
				return true;
			}else{
				return false;
			}
		},
		message : '只能输入{0}个文字！'
	},
	filenameExtension : {
		validator : function(value, param){
			var result = false;
			var filenameExt = value.substr(value.lastIndexOf('.') + 1, value.length).toLowerCase();
			for(var i = 0 ; i < param.length ; i++){
				if(filenameExt == param[i]){
					result = true;
					break;
				}
			}
			var msg = '只能上传';
			for(var i = 0 ; i < param.length ; i++){
				msg += param[i]+'、';
			}
			$.fn.validatebox.defaults.rules.filenameExtension.message =msg.substr(0,msg.length - 1);
			return result;
		},
		message : ''
	}
});

$.extend($.fn.datagrid.defaults.editors, {
	combocheckboxtree : {
		init : function(container, options) {
			var editor = $('<input/>').appendTo(container);
			options.multiple = true;
			editor.combotree(options);
			return editor;
		},
		destroy : function(target) {
			$(target).combotree('destroy');
		},
		getValue : function(target) {
			return $(target).combotree('getValues').join(',');
		},
		setValue : function(target, value) {
			$(target).combotree('setValues', sy.getList(value));
		},
		resize : function(target, width) {
			$(target).combotree('resize', width);
		}
	},
	mytextarea : {
		init : function(container, options) {
			var editor = $('<textarea rows="8" cols="30"></textarea>').appendTo(container);
			return editor;
		},
		getValue : function(target) {
			return $(target).val().replace(new RegExp("\n", "gm"),"<br/>");
		},
		setValue : function(target, value) {
			$(target).val(value.replace(new RegExp("<br/>", "gm"),"\n"));
		},
		resize: function(target, width){  
            var input = $(target);  
            if ($.boxModel == true){  
                input.width(width - (input.outerWidth() - input.width()));  
            } else {  
                input.width(width);  
            }  
        }  
	},
	customDatebox: {
        init: function(container, options){
            var input = $('<input type="text" readonly="readonly" />').appendTo(container);
            $(input).attr("id", ++$.datepicker.uuid).removeClass("hasDatepicker").datepicker({  
				changeMonth: true,
			    changeYear: true,
			    yearRange: '1911:'+new Date().getFullYear(),
			    dateFormat: "yy-mm-dd",
			    dayNamesMin:["日","一","二","三","四","五","六"],
			    firstDay: 0,
			    nextText: "下一月",
			    prevText: "上一月",
			    closeText: "关闭",
			    currentText: "今天",
			    monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			    isRTL: false,
			    showOn:"button",
			    buttonImage: sy.bp()+"/images/jquery-ui/calendar.gif",
				buttonImageOnly: true ,
				beforeShow: function () {
			        setTimeout(
			            function () {
			            	$("#ui-datepicker-div").css('font-size','15'); //改變大小
			            	$('#ui-datepicker-div').css('z-index', '300');
			            }, 100
			        );
			    }
		    });  
            return input;
        },
        getValue: function(target){  
            return $(target).val();  
        },  
        setValue: function(target, value){  
            $(target).val(value);  
        }, 
        resize: function(target, width){
        	var input = $(target);  
            if ($.boxModel == true){  
                input.width(width - (input.outerWidth() - input.width()) - 20);  
            } else {  
                input.width(width-20);  
            } 
        }
    },
	combobox: {
		init: function(container, options){
			var combo = $('<input type="text">').appendTo(container);
			combo.combobox(options || {});
			return combo;
		},
		destroy: function(target){
			$(target).combobox('destroy');
		},
		getValue: function(target){
			var opts = $(target).combobox('options');
			if (opts.multiple){
				return $(target).combobox('getValues').join(opts.separator);
			} else {
				return $(target).combobox('getValue');
			}
		},
		setValue: function(target, value){
			var opts = $(target).combobox('options');
			if (opts.multiple){
				if (value == ''){
					$(target).combobox('clear');
				} else {
					$(target).combobox('setValues', value.split(opts.separator));
				}
			} else {
				$(target).combobox('setValue', value);
			}
		},
		resize: function(target, width){
			$(target).combobox('resize', width);
		}
	}
});

/**
 * 获得专案根目录
 * 使用方法：sy.bp();
 */
sy.bp = function() {
	var curWwwPath = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var localhostPaht = curWwwPath.substring(0, pos);
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	return (localhostPaht + projectName);
};

/**
 * 增加formatString功能
 * 使用方法：sy.fs('字串{0}字串{1}字串','第一個變數','第二個變數');
 */
sy.fs = function(str) {
	for ( var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};

/**
 * 增加命名空間功能
 * 使用方法：sy.ns('jQuery.bbb.ccc','jQuery.eee.fff');
 */
sy.ns = function() {
	var o = {}, d;
	for ( var i = 0; i < arguments.length; i++) {
		d = arguments[i].split(".");
		o = window[d[0]] = window[d[0]] || {};
		for ( var k = 0; k < d.slice(1).length; k++) {
			o = o[d[k + 1]] = o[d[k + 1]] || {};
		}
	}
	return o;
};

/**
 * 生成UUID
 */
sy.random4 = function() {
	return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
};
sy.UUID = function() {
	return (sy.random4() + sy.random4() + "-" + sy.random4() + "-" + sy.random4() + "-" + sy.random4() + "-" + sy.random4() + sy.random4() + sy.random4());
};

/**
 * 獲取URL參數
 */
sy.getUrlParam = function(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
};

sy.getList = function(value) {
	if (value) {
		var values = [];
		var t = value.split(',');
		for ( var i = 0; i < t.length; i++) {
			values.push('' + t[i]);/* 避免將ID當成數字 */
		}
		return values;
	} else {
		return [];
	}
};

sy.png = function() {
	var imgArr = document.getElementsByTagName("IMG");
	for ( var i = 0; i < imgArr.length; i++) {
		if (imgArr[i].src.toLowerCase().lastIndexOf(".png") != -1) {
			imgArr[i].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + imgArr[i].src + "', sizingMethod='auto')";
			imgArr[i].src = "images/blank.gif";
		}
		if (imgArr[i].currentStyle.backgroundImage.lastIndexOf(".png") != -1) {
			var img = imgArr[i].currentStyle.backgroundImage.substring(5, imgArr[i].currentStyle.backgroundImage.length - 2);
			imgArr[i].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + img + "', sizingMethod='crop')";
			imgArr[i].style.backgroundImage = "url('images/blank.gif')";
		}
	}
};

sy.bgPng = function(bgElements) {
	for ( var i = 0; i < bgElements.length; i++) {
		if (bgElements[i].currentStyle.backgroundImage.lastIndexOf(".png") != -1) {
			var img = bgElements[i].currentStyle.backgroundImage.substring(5, bgElements[i].currentStyle.backgroundImage.length - 2);
			bgElements[i].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + img + "', sizingMethod='crop')";
			bgElements[i].style.backgroundImage = "url('images/blank.gif')";
		}
	}
};

sy.isLessThanIe8 = function() {/* 判斷瀏覽器是否是IE且版本小於8 */
	return ($.browser.msie && $.browser.version < 8);
};