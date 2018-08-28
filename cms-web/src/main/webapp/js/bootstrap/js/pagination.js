/**
 * Created by zmy on 2016/7/13.
 */

var paginationMaxLength=10;//分页栏的最大显示条数
var onlyOnePageIsShow = true;//只有一页的时候是否显示
function paginationInit(){
    $('[pagination =pagination_new ]').each(function(){//获取点击的div pagination的name值，确定容器,绑定事件
        initPagination($(this));
    });
}
function isNeedPagination(totalpage,settingfromHTML){
    var condition ;
    if(settingfromHTML == "true"){
        condition = true;
    }else if(settingfromHTML == 'false'){
        condition = false;
    }else {
        condition = onlyOnePageIsShow;
    }
    if(condition && totalpage<1){
        return false;
    }else if(!condition && totalpage<=1){
        return false;
    }
    return true;
}
//设置显示的页数
function setDisplayMaxLength(element,len){
    var currentpage =  Number(element.attr('pagenumber'));//当前页
    var totoalpage = Number(element.attr('totalpage'));//总页数
    if(checkParamIsPositiveInteger(len)){
        len = Number(len);//自定义显示页数
    }else{
        len =paginationMaxLength;//最多十条
    }
    if(len<totoalpage){
        var temp1 = parseInt((len-1)/2);
        var temp2 = parseInt(len/2);
        if(temp1<temp2){
            var leftstart = currentpage - temp1;
            var rightend = currentpage + temp1 + 1;
        }else{
            var leftstart = currentpage - temp1;
            var rightend = currentpage + temp1;
        }
        if(leftstart<1){
            rightend +=(1-leftstart);
            leftstart = 1;
        }
        if(rightend>totoalpage){
            if(leftstart>1){
                leftstart -=(rightend-totoalpage);
            }
            rightend =totoalpage;
        }
        if(leftstart<1){
            leftstart=1
        }
        while(leftstart >1){
            element.children('ul').children('li[value = '+(--leftstart)+']').css('display','none');//数据左移
        }
        while(rightend <totoalpage){
            element.children('ul').children('li[value = '+(++rightend)+']').css('display','none');//数据右移
        	//element.children('ul').children('li[value = '+(++rightend)+']').append('<li value="'+i+'"><a href="javascript:void(0);">'+i+'</a></li>');
        }
    }
}
//根据页面pagenumber、my_totoalpage更新分页，参数element传的是分页的容器
function initPagination(element){
    element.html('');
    var pagenumber = element.attr('pagenumber');
    var totalpage = element.attr('totalpage');
    var pMaxLength = element.attr('paginationMaxLength');
    var onePageIsShow = element.attr('onlyOnePageIsShow');//只有一页
    if(isNeedPagination(Number(totalpage),onePageIsShow)){
        var content = '<ul class="pagination"><li value="0"><a href="javascript:void(0);">«</a></li>';//小于总页数有上一页
        for(var i =1; i<=totalpage;i++){//遍历总页数
            content +='<li value="'+i+'"><a href="javascript:void(0);">'+i+'</a></li>'
         /* if(i=totalpage){
          * <li value="1"><a href="javascript:void(0);">首页</a></li>
            	content +='<li value="'+i+'"><a href="javascript:void(0);">尾页</a></li>'	
            }*/
        }
        content +='<li value="-1"><a href="javascript:void(0);">»</a></li></ul>';//下一页
        element.append(content);
        element.children('ul').children('li[value='+pagenumber+']').attr('class','active');
        setDisplayMaxLength(element,pMaxLength);
        addClickListener(element);
    }
}
//页数变换
function addClickListener(element){
    element.children('ul').children('li').bind('click',function(){
        var temp = Number($(this).attr('value'));
        var pagenumber = Number($(this).parent().parent().attr('pagenumber'));
        var totalpage = Number($(this).parent().parent().attr('totalpage'));
        if(temp == 0){
            temp = pagenumber-1;
        }else if(temp == -1){
            temp = pagenumber+1;
        }
        if(temp != pagenumber && temp !=0 && temp<=totalpage){
            $(this).parent().parent().attr('pagenumber',temp);
            paginationClick(element.attr("id"));
            initPagination(element);
        }
        return false;
    })
}
function checkParamIsPositiveInteger(param){
    var reg = /^[1-9]+[0-9]*]*$/;
    return reg.test(param);
}

//用户需要自己实现的点击事件，参数为分页容器的id
function paginationClick(pagination_id){

}
$(function(){
    paginationInit();
});