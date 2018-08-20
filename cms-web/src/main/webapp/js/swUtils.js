document.onkeydown=function(ev) {
	var oEvent=ev||event;
	if(oEvent.keyCode==27){
		$('.easyui-dialog').each(function(i){
			$(this).dialog('close');
		});
	}
}

var cookieList = function(cookieName) {
	var cookie = $.cookie(cookieName);
	var items = cookie ? cookie.split(/,/) : new Array();
	return {
		"contains" : function(val){
			for(var i = 0 ; i < items.length ; i++){
				if(val == items[i]){
					return true;
				}
			}
			return false;
		},
	    "add": function(val) {//Add to the items.
	        items.push(val);
	        $.cookie(cookieName, items.join(','));
	    },
	    "remove": function (val) {
	        /** indexOf not support in IE, and I add the below code **/
	        if (!Array.prototype.indexOf) {
	            Array.prototype.indexOf = function(obj, start) {
	                for (var i = (start || 0), j = this.length; i < j; i++) {
	                    if (this[i] === obj) { return i; }
	                }
	                return -1;
	            };
	        }
	        var indx = items.indexOf(val);
	        if(indx!=-1) items.splice(indx, 1);
	        $.cookie(cookieName, items.join(','));
	    },
	    "clear": function() {//clear the cookie.
	        items = null;
	        $.cookie(cookieName, null);
	    },
	    "items": function() {//Get all the items.
	        return items;
	    }
	};
};
var ezuiDialogClose = function(dialogId){
	$(dialogId).dialog('close');
};
var ezuiCombotreeReload = function(ezuiForm){
	$(ezuiForm).find('.easyui-combotree').each(function(){
		$(this).combotree('reload');
	});
};
var ezuiComboboxReload = function(ezuiForm){
	$(ezuiForm).find('.easyui-combobox').each(function(){
		$(this).combobox('reload');
	});
};
var ezuiCombotreeClear = function(ezuiForm){
	$(ezuiForm).find('.easyui-combotree').each(function(){
		$(this).combotree('clear');
	});
};
var ezuiTimespinnerClear = function(ezuiForm){
	$(ezuiForm).find('.easyui-datetimespinner').each(function(){
		$(this).spinner('clear');
	});
	$(ezuiForm).find('.easyui-timespinner').each(function(){
		$(this).timespinner('clear');
	});
};
var ezuiFileboxClear = function(ezuiForm){
	$(ezuiForm).find('.easyui-filebox').each(function(){
		$(this).filebox('clear');
	});
};
var ezuiComboboxClear = function(ezuiForm){
	$(ezuiForm).find('.easyui-combobox').each(function(){
		$(this).combobox('clear');
	});
};
var ezuiTextboxClear = function(ezuiForm){
	$(ezuiForm).find('.easyui-textbox').each(function(){
		$(this).textbox('clear');
	});
};
var ezuiNumberboxClear = function(ezuiForm){
	$(ezuiForm).find('.easyui-numberbox').each(function(){
		$(this).numberbox('clear');
	});
};
var ezuiToolbarDatagridClear = function(toolbarId, ezuiDatagrId){
	ezuiFormClear($(toolbarId));
	if(ezuiDatagrId){
		$(ezuiDatagrId).datagrid('loadData',{total:0,rows:[]});
	}
};
var ezuiToolbarClear = function(toolbarId, ezuiDatagrId){
	ezuiFormClear($(toolbarId));
	if(ezuiDatagrId){
		$(ezuiDatagrId).datagrid('load', {});
	}
};
var ezuiToolbarClearForDefaultValue = function(toolbarId, ezuiDatagrId, data){
	ezuiFormClear($(toolbarId));
	if(ezuiDatagrId){
		$(ezuiDatagrId).datagrid('load', data);
	}
};
var ezuiDateboxClear = function(ezuiForm){
	$(ezuiForm).find('.easyui-datebox').each(function(){
		$(this).datebox('clear');
		$(this).datebox('calendar').calendar({
            validator: function(date){
                return true;
            }
        });
	});
};
var ezuiDatetimeboxClear = function(ezuiForm){
	$(ezuiForm).find('.easyui-datetimebox').each(function(){
		$(this).datetimebox('clear');
		$(this).datetimebox('calendar').calendar({
            validator: function(date){
                return true;
            }
        });
	});
};
var ezuiFormClear = function(ezuiForm){
	ezuiTimespinnerClear(ezuiForm);
	ezuiFileboxClear(ezuiForm);
	ezuiNumberboxClear(ezuiForm);
	ezuiTextboxClear(ezuiForm);
	ezuiComboboxClear(ezuiForm);
	ezuiCombotreeClear(ezuiForm);
	ezuiDateboxClear(ezuiForm);
	ezuiDatetimeboxClear(ezuiForm);
	$(ezuiForm).find('input[type=file]').each(function(){
		$(this).val('');
	});
	$(ezuiForm).find('input[type=checkbox]').each(function(){
		$(this).attr('checked',false);
	});
	$(ezuiForm).find('input[type=radio]').each(function(){
		$(this).attr('checked',false);
	});
	$(ezuiForm).find('input').each(function(){
		$(this).removeClass('tooltip-f');
		$(this).removeClass('validatebox-invalid');
	});
};
var clearDatagridSelected = function(datagridId){
	$(datagridId).datagrid('unselectAll');
};
var clearTreegridSelected = function(treegridId){
	$(treegridId).treegrid('unselectAll');
};
var ajaxBtn = function(menuId, url, datagridMenu){
	var btnArray;
	$.ajax({
		url : 'btnController.do?findAll',
		type : 'POST', dataType : 'JSON',async  :false,
		success : function(result){
			btnArray = result.obj.split(",");
		}
	});

	$.ajax({
		url : url,
		data : {id : menuId},type : 'POST', dataType : 'JSON',async  :false,
		success : function(result){
			var flag = "";
			for(var i = 0 ; i < btnArray.length ; i++){
				flag = result.obj.indexOf(btnArray[i]) == -1 ? 'disable' : 'enable';
				if($('#ezuiBtn_'+btnArray[i]) && $('#ezuiBtn_'+btnArray[i]).length > 0){
					$('#ezuiBtn_'+btnArray[i]).linkbutton(result.obj.indexOf(btnArray[i]) == -1 ? "disable" : "enable");
					if(result.obj.indexOf(btnArray[i]) == -1){
						$('#ezuiBtn_'+btnArray[i]).hide();
					}else{
						$('#ezuiBtn_'+btnArray[i]).show();
					}
				}
				if($('#ezuiCombobox_'+btnArray[i]) && $('#ezuiCombobox_'+btnArray[i]).length > 0){
					$('#ezuiCombobox_'+btnArray[i]).combobox(result.obj.indexOf(btnArray[i]) == -1 ? "disable" : "enable");
				}
				if($('#ezuiNumberbox_'+btnArray[i]) && $('#ezuiNumberbox_'+btnArray[i]).length > 0){
					$('#ezuiNumberbox_'+btnArray[i]).combobox(result.obj.indexOf(btnArray[i]) == -1 ? "disable" : "enable");
				}
				if(datagridMenu && $('#menu_'+btnArray[i]) && $('#menu_'+btnArray[i]).length > 0){
					$(datagridMenu).menu(flag+'Item', $('#menu_'+btnArray[i])[0]);
				}
			}
		}
	});
};
function arrayRemoveDuplicate(arrayObj){
	var newArray = [];
	$.each(arrayObj, function(i, el){
	    if($.inArray(el, newArray) === -1) newArray.push(el);
	});
	newArray.sort(function(i,j) { return (i-j); });
	return newArray;
}
//myArray : 欲查询之阵列
//searchTerm ：查询的值
//property：查询阵列中物件的字段
function arrayObjectIndexOf(myArray, searchTerm, property) {//判断阵列中指定物件的名称跟值是否存在
    for(var i = 0, len = myArray.length; i < len; i++) {
        if (myArray[i][property] === searchTerm) return i;
    }
    return -1;
}
function formatDate(date,format){
	var paddNum = function(num){
	  num += "";
	  return num.replace(/^(\d)$/,"0$1");
	}
	//指定格式字符
	var cfg = {
	   yyyy : date.getFullYear() //年 : 4位
	  ,yy : date.getFullYear().toString().substring(2)//年 : 2位
	  ,M  : date.getMonth() + 1  //月 : 如果1位的时候不补0
	  ,MM : paddNum(date.getMonth() + 1) //月 : 如果1位的时候补0
	  ,d  : date.getDate()   //日 : 如果1位的时候不补0
	  ,dd : paddNum(date.getDate())//日 : 如果1位的时候补0
	  ,hh : date.getHours()  //时
	  ,mm : date.getMinutes() //分
	  ,ss : date.getSeconds() //秒
	}
	format || (format = "yyyy-MM-dd");
	return format.replace(/([a-z])(\1)*/ig,function(m){return cfg[m];});
}
function formatter2(date){
    if (!date){return '';}
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    return y + '-' + (m<10?('0'+m):m);
}
function parser2(s){
    if (!s){return null;}
    var ss = s.split('-');
    var y = parseInt(ss[0],10);
    var m = parseInt(ss[1],10);
    if (!isNaN(y) && !isNaN(m)){
        return new Date(y,m-1,1);
    } else {
        return new Date();
    }
}
function diffArray (a1, a2) {
    var a = [], diff = [];
    for (var i = 0; i < a1.length; i++) {
        a[a1[i]] = true;
    }
    for (var i = 0; i < a2.length; i++) {
        if (a[a2[i]]) {
            delete a[a2[i]];
        } else {
            a[a2[i]] = true;
        }
    }
    for (var k in a) {
        diff.push(k);
    }
    return diff;
};
function removeHTMLTag(str) {
	str = str.replace(/<\/?[^>]*>/g,''); //去除HTML tag
	str = str.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
	//str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
	str=str.replace(/ /ig,'');//去掉
	return str;
}
var getObjArrayValues = function(objArray){
	var valueArray = [];
	$(objArray).each(function(){
		valueArray.push($(this).val());
	});
	return valueArray.join(',');
};
var copyTextToClipboard = function (text) {//将指定文字复制到剪贴版
	var textArea = document.createElement("textarea");
	textArea.style.position = 'fixed';
	textArea.style.top = 0;
	textArea.style.left = 0;
	textArea.style.width = '2em';
	textArea.style.height = '2em';
	textArea.style.padding = 0;
	textArea.style.border = 'none';
	textArea.style.outline = 'none';
	textArea.style.boxShadow = 'none';
	textArea.style.background = 'transparent';
	textArea.value = text;
	document.body.appendChild(textArea);
	textArea.select();
	try {
		var successful = document.execCommand('copy');
		var msg = successful ? 'successful' : 'unsuccessful';
	} catch (err) {
		console.log('复制失败！');
	}
	document.body.removeChild(textArea);
};
/**
 * ajax download
 * @param url 路径
 * @param paramMap 参数HashMap
 * @returns {String} formId
 */
var ajaxDownloadFile = function(url,paramMap){
	var token = new Date().getTime();
	var formId = 'ajaxDownload_'+token;
	
	var form = $('<form style="display:none" target="" method="post">'); 
	form.attr('id',formId);   
	form.attr('action',url);
	if(paramMap != null && paramMap.size() > 0){
		var keys = paramMap.keys();
		for(var i = 0 ; i < keys.length ; i++){ 
			input0 = $('<input type="hidden">'); 
			input0.attr('name',keys[i]); 
			input0.attr('value',paramMap.get(keys[i])); 
			form.append(input0);
		} 
	}
	$('body').append(form);
	form.submit(); 
	return formId;
};
//添加Laoding遮罩
function completeLoading() {  
    if (document.readyState == "complete") {  
        var loadingMask = document.getElementById('loadingDiv');  
        loadingMask.parentNode.removeChild(loadingMask);  
    }  
}
function makeMask(){
	//获取浏览器页面可见高度和宽度  
    var _PageHeight = document.documentElement.clientHeight,  
        _PageWidth = document.documentElement.clientWidth;  
    //计算loading框距离顶部和左部的距离（loading框的宽度为215px，高度为61px）  
    var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0,  
        _LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2 : 0;   
    //在页面未加载完毕之前显示的loading Html自定义内容  
    var _LoadingHtml = '<div id="loadingDiv" style="position:absolute;left:0;width:100%;height:' + _PageHeight + 'px;top:0;background:#243543;opacity:1;filter:alpha(opacity=100);z-index:10000;"><div style="position: absolute; cursor1: wait; left: ' + _LoadingLeft + 'px; top:' + _LoadingTop + 'px; width: auto; height: 57px; line-height: 57px; padding-left: 10px; padding-right: 5px; color: #fff ; border: 2px solid #95B8E7; font-family:\'Microsoft YaHei\';">页面加载中，请等待...</div></div>';  
    //呈现loading效果  
    document.write(_LoadingHtml);  
}
function HashMap(){
	var size = 0;/** Map 大小 * */
	var entry = new Object();/** 对象 * */
	this.put = function(key, value){/** 存 * */
		if (!this.containsKey(key)){
			size++;
		}
		entry[key] = value;
	};
	this.get = function(key){/** 取 * */
		if (this.containsKey(key)){
			return entry[key];
		}else{
			return null;
		}
	};
	this.remove = function(key){/** 删除 * */
		if (delete entry[key]){
			size--;
		}
	};
	this.containsKey = function(key){/** 是否包含 Key * */
		return (key in entry);
	};
	this.containsValue = function(value){/** 是否包含 Value * */
		for (var prop in entry){
			if (entry[prop] == value){
				return true;
			}
		}
		return false;
	};
	this.values = function(){/** 所有 Value * */
		var values = [];
		for (var prop in entry){
			values.push(entry[prop]);
		}
		return values;
	};
	this.keys = function(){/** 所有 Key * */
		var keys = [];
		for (var prop in entry){
			keys.push(prop);
		}
		return keys;
	};
	this.size = function(){/** Map Size * */
		return size;
	};
}
var charArray = ['A','B','C','D','E','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
String.prototype.halfToFull = function () {
    var temp = "";
    for (var i = 0; i < this.toString().length; i++) {
        var charCode = this.toString().charCodeAt(i);
        if (charCode >= 33 && charCode <= 126) {
            charCode += 65248;
        } else if (charCode == 32) { // 半形空白轉全形
            charCode = 12288;
        }
        temp = temp + String.fromCharCode(charCode);
    }
    return temp;
};
$.fn.fieldToUpperCase = function(){// 欄位英文變大寫
	$(this).blur(function(){
		$(this).val($(this).val().toUpperCase());
	});
};
$.fn.datebox.defaults.formatter = function(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return  y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);
};

$.fn.datebox.defaults.parser = function(s) {
	if (s) {
		if(s.indexOf(' ') > -1){
			var a = s.split(' ');
			var y = new Number(a[0].split('-')[0]);
			var m = new Number(a[0].split('-')[1]);
			var d = new Number(a[0].split('-')[2]);
			
			var h = new Number(a[1].split(':')[0]);
			var mm = new Number(a[1].split(':')[1]);
			if(a[1].split(':')[2]){
				var s = new Number(a[1].split(':')[2]);
				return new Date(y, m-1, d, h, mm, s);
			}else{
				return new Date(y, m-1, d, h, mm);
			}
		}else{
			var a = s.split('-');
			var y = new Number(a[0]);
			var m = new Number(a[1]);
			var d = new Number(a[2]);
			return new Date(y, m-1, d);
		}
	} else {
		return new Date();
	}
};