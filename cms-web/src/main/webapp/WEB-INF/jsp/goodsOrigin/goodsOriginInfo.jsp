<%@ page language='java' pageEncoding='UTF-8'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://www.springframework.org/tags' prefix='spring'%>
<style type="text/css">
		.clearFile {position:relative; display:block;text-indent: 0;float:left;  margin-top:70px; margin-left : 20px;   border-radius:5px;   width: 80px;  color:#ffffff; background: #1b94ff;   text-decoration:none;  line-height:30px; text-align:center;    height: 30px; }
		.clearFile:hover{text-decoration: none;   background: #6a96df; color: white;}
		.file {position:relative; display:block;text-indent: 0;float:left;  margin-top:70px; margin-left:20px; border-radius:5px;   width: 80px;  color:#ffffff; background: #1b94ff;   text-decoration:none;  line-height:30px; text-align:center;    height: 30px; }
		.file:hover{text-decoration: none;   background: #6a96df; color: white;}
		.file input {position: absolute; width: 183px; height: 48px;     right: 0px; top: 0; opacity: 0; }
		.sfzimg{width: 100px; height: 100px; float:left;  padding:1px; display: block; border-radius:5px; border: 2px solid #48aada; }
</style>
<script>
function clearFile(id,inputid){
	$("#"+id).html("");
	$("#"+inputid).val("");
}
//文件上传 预览
function upload(id,inputid,url){   
	var path = $("#fileImg").val();
    if (path.length == 0) {
        return false;
    } else {
        var extStart = path.lastIndexOf('.'),
            ext = path.substring(extStart, path.length).toUpperCase();
        if (ext !== '.PNG' && ext !== '.JPG' && ext !== '.JPEG' && ext !== '.GIF') {
        	$.messager.show({
				msg : "只能上传图片", title : '<spring:message code="common.message.prompt"/>'
			});
            return false;
        }
    }
	// 文件元素
    var file = document.querySelector('#fileImg');
    // 通过FormData将文件转成二进制数据
    var formData = new FormData();
    // 将文件转二进制
    var a=file.files[0];
    if(file.files[0]!=undefined){
    formData.append('file', file.files[0]);
    formData.append('ext',ext);
    $.ajax({  
        url : url,  
        data : formData,  
        type : 'post',  
        processData:false,  
        contentType:false,  
        dataType : 'JSON',
        success : function(data){
	        if(!data.success){
	        	$.messager.show({
					msg : data.msg, title : '<spring:message code="common.message.prompt"/>'
				});
	        	$("#"+id).html("");
	        	$("#"+inputid).val("");
	        }else{
	        	$("#"+id).html("<img width='100' height='100' src="+data.obj+">");
	        	$("#"+inputid).val(data.obj);
	        }
        }
    });  
    }else{
    	ShowInfo("未选择文件");
    	$("#"+id).html("");
    	$("#"+inputid).val("");
    }
}


var commitGoodsOriginInfo = function(){
	var url = '';
	if (processType == 'edit') {
		url = '<c:url value="/goodsOriginController.do?edit"/>';
	}else{
		url = '<c:url value="/goodsOriginController.do?add"/>';
	}
	$('#ezuiGoodsOriginInfoForm').form('submit', {
		url : url,
		onSubmit : function(){
			if($('#ezuiGoodsOriginInfoForm').form('validate')){
				$.messager.progress({
					text : '<spring:message code="common.message.data.processing"/>', interval : 100
				});
				return true;
			}else{
				return false;
			}
		},
		success : function(data) {
			var msg='';
			try {
				var result = $.parseJSON(data);
				if(result.success){
					msg = result.msg;
					ezuiDatagrid.datagrid('reload');
					ezuiDialog.dialog('close');
				}else{
					msg = '<font color="red">' + result.msg + '</font>';
				}
			} catch (e) {
				msg = '<font color="red">' + JSON.stringify(data).split('description')[1].split('</u>')[0].split('<u>')[1] + '</font>';
				msg = '<spring:message code="common.message.data.process.failed"/><br/>'+ msg;
			} finally {
				$.messager.show({
					msg : msg, title : '<spring:message code="common.message.prompt"/>'
				});
				$.messager.progress('close');
			}
		}
	});
};
</script>
<div id='ezuiGoodsOriginInfoDialog' style='padding: 10px;'>
		<form id='ezuiGoodsOriginInfoForm' method='post' enctype="multipart/form-data" >
			<input type='hidden' id='goodsId' name='goodsId'/>
			<table>
				<tr>
					<th>商品名称<font color="red">*</font>：</th>
					<td colspan="3"><input type='text' name='goodsName' class='easyui-textbox' style='width: 389px;' disabled="disabled" data-options='required:true'/></td>  
					<th>品类<font color="red">*</font>：</th>
					<td><input type='text' name='goodsType' id='goodsType' class='easyui-combobox' size='20'  data-options="
									required:true,
									panelHeight:'auto',
									panelMaxHeight:200,
									editable:false, 
									valueField:'id',
									textField:'value',
									url:'<c:url value="goodsOriginController.do?getGoodsOriginTypeCombobox"/>', 
									"/></td>
				</tr>
				<tr>
					<th>产地<font color="red">*</font>：</th>
					<td><input type='text' name='goodsOrigin' disabled="disabled" class='easyui-textbox' size='20'  data-options='required:true'/></td>
					<th>规格<font color="red">*</font>：</th>
					<td><input type='text' name='goodsStandard' disabled="disabled" class='easyui-textbox' size='20' data-options='required:true'/></td>
				</tr>
				<tr>
					<th>采购类型<font color="red">*</font>：</th>
					<td><input type='text' name='purchaseType' class='easyui-combobox' size='20'  data-options="
							required:true,
							panelHeight:'auto',
							panelMaxHeight:200,
							editable:false,
							required:true,
							valueField:'id',
							textField:'value',
							url:'<c:url value="/dataDictionaryController.do?getCombobox&type=purchaseType"/>'"/></td> 
					<th>存储方式：</th>
					<td><input type='text' name='goodsStorage' class='easyui-textbox' size='20' /></td>
					<th>存储类别<font color="red">*</font>：</th>
					<td><input type='text' name='storageType' class='easyui-combobox' size='20'  data-options="
							required:true,
							panelHeight:'auto',
							panelMaxHeight:200,
							editable:false,
							required:true,
							valueField:'id',
							textField:'value',
							url:'<c:url value="/dataDictionaryController.do?getCombobox&type=storageType"/>'"/></td>
				</tr>
				<tr>
					<th>状态：</th>
					<td> 
						<span class="radioSpan">
			                <input type="radio" name="status" value="0" />有效 
			                <input type="radio" name="status" style='margin-left: 30px;' value="1" />无效 
            			</span>
            		</td>
				</tr>
				<tr>
					<th>缩减图：</th>
					<td colspan="4">
					<div id="imgFile" class="sfzimg"></div>
					<div class="images"> 
						<a href="javascript:;" class="file">选择图片
						<input type="file" name="goodsImageFile" id="fileImg"
							onchange="upload('imgFile','goodsImage','goodsOriginController.do?upload')">
							<input type="hidden" id="goodsImage" name="goodsImage">
						</a>
					</div>
					<a href="javascript:;" class='clearFile' onclick="clearFile('imgFile','goodsImage')" >清空图片</a>
					<!-- <img width="150" height="150" id='images' src=''>
						<input type='hidden' name='goodsImage' />  -->
					</td>
				</tr>
				<tr>
					<th>商品描述：</th>
					<td colspan="4"> 
					<input class="easyui-textbox"  name='goodsDesc' data-options="multiline:true,validType:'length[300]',invalidMessage:'字数不能超过300' "   style="width:388px;height:100px">
					</td>
				</tr>
				<tr>
					<th>保质期：</th>
					<td><input type='text' name='expiryDate' id='expiryDate' class='easyui-numberbox'  max='999' min='0' precision='0' size='3' />
						<input type='text' name='expiryDateUnit' id='expiryDateUnit' class='easyui-combobox' size='5' data-options="
							panelHeight:'auto',
							panelMaxHeight:200,
							editable:false,
							valueField: 'id',
							textField: 'value',
							data: [
								{id: '年', value: '年' , select:true},
								{id: '月', value: '月' },
								{id: '天', value: '天' }
							],
							onSelect:function(){
								$('#expiryDate').textbox('setValue','');
							}"/></td>
					<td colspan="4"><font color="red">保质期只能填写整数，每月为30天，每年为365天</font></td>
				</tr> 
				<tr>
					<td colspan="5"></td>
					<td><a onclick='commitGoodsOriginInfo();' id='ezuiBtn_commitGoodsOriginInfo' class='easyui-linkbutton' style='margin-left: 20px;' href='javascript:void(0);'><spring:message code='common.button.commit'/></a>
					<a onclick='ezuiDialogClose("#ezuiGoodsOriginInfoDialog");' id='ezuiBtn_ezuiDialogClose' class='easyui-linkbutton' style='margin-left: 20px;'  href='javascript:void(0);'><spring:message code='common.button.close'/></a>
					</td>
				</tr>
			</table>
		</form>
	</div>