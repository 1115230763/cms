package com.cms.utils.generator;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;

import com.cms.entity.Btn;

public class CodeGenerator {
	private static final String VO_PATH = "D:\\CodeGenerator\\";
	private static final String FORM_PATH = "D:\\CodeGenerator\\";
	private static final String QUERY_PATH = "D:\\CodeGenerator\\";
	private static final String DAO_PATH = "D:\\CodeGenerator\\";
	private static final String SERVICE_PATH = "D:\\CodeGenerator\\";
	private static final String CONTROLLER_PATH = "D:\\CodeGenerator\\";
	private static final String JSP_PATH = "D:\\CodeGenerator\\";

	private static final String VO_PACKAGE_NAME = "com.cms.vo";
	private static final String FORM_PACKAGE_NAME = "com.cms.vo.form";
	private static final String QUERY_PACKAGE_NAME = "com.cms.query";
	private static final String DAO_PACKAGE_NAME = "com.cms.dao";
	private static final String SERVICE_PACKAGE_NAME = "com.cms.service";
	private static final String CONTROLLER_PACKAGE_NAME = "com.cms.controller";
	
 
	private static final Boolean IS_NEED_COMBOBOX = true; 
	   
 
	  
	public static void main(String[] args) {
 		CodeGenerator generator = new CodeGenerator(new Btn());
		generator.genData(); 
	}
	private Object obj;
	public CodeGenerator(Object obj){
		this.obj = obj;
	}
	
	public void genData(){
		StringBuilder sb = new StringBuilder();
		
		String objClsName = obj.getClass().getName();
		objClsName = objClsName.substring(objClsName.lastIndexOf(".") + 1, objClsName.length());
		
		try (FileWriter fw = new FileWriter(new File(sb.append(VO_PATH).append(objClsName).append("VO.java").toString()))) {
			fw.write(this.voGenerator(objClsName, false, false));
			fw.flush();
			sb.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		try (FileWriter fw = new FileWriter(new File(sb.append(FORM_PATH).append(objClsName).append("Form.java").toString()))) {
			fw.write(this.voGenerator(objClsName, true, false));
			fw.flush();
			sb.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		try (FileWriter fw = new FileWriter(new File(sb.append(QUERY_PATH).append(objClsName).append("Query.java").toString()))) {
			fw.write(this.voGenerator(objClsName, false, true));
			fw.flush();
			sb.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try (FileWriter fw = new FileWriter(new File(sb.append(DAO_PATH).append(objClsName).append("Dao.java").toString()))) {
			fw.write(this.daoGenerator(objClsName));
			fw.flush();
			sb.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		try (FileWriter fw = new FileWriter(new File(sb.append(SERVICE_PATH).append(objClsName).append("Service.java").toString()))) {
			fw.write(this.serviceGenerator(objClsName));
			fw.flush();
			sb.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		try (FileWriter fw = new FileWriter(new File(sb.append(CONTROLLER_PATH).append(objClsName).append("Controller.java").toString()))) {
			fw.write(this.controllerGenerator(objClsName));
			fw.flush();
			sb.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		String str = objClsName.replaceFirst(String.valueOf(objClsName.charAt(0)), String.valueOf(objClsName.charAt(0)).toLowerCase());
		File directory = new File(sb.append(JSP_PATH).append("//").append(str).toString());sb.setLength(0);
		if(!directory.exists()){
			directory.mkdir();
		}
		try (FileWriter fw = new FileWriter(new File(sb.append(JSP_PATH).append("//").append(str).append("//main.jsp").toString()))) {
			fw.write(this.jspGenerator(objClsName));
			fw.flush();
			sb.setLength(0);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 建一個vo.java
	 * @param objClsName
	 * @return
	 */
	public String voGenerator(String objClsName, boolean isForm, boolean isQuery){
		StringBuilder sb = new StringBuilder();
		String firstCharUpperFieldName = null;
		String typeName = null;
		String str = objClsName.replaceFirst(String.valueOf(objClsName.charAt(0)), String.valueOf(objClsName.charAt(0)).toLowerCase());
		
		sb.append("package ");
		if(isForm){
			sb.append(FORM_PACKAGE_NAME);
		}else if(isQuery){
			sb.append(QUERY_PACKAGE_NAME);
		}else if(!isForm && !isQuery){
			sb.append(VO_PACKAGE_NAME);
		}
		
		sb.append(";").append("\n\n")
		  .append("import org.codehaus.jackson.map.annotate.JsonSerialize;\n")
		  .append("import com.cms.utils.serialzer.JsonDatetimeSerializer;\n").append("\n")
		  .append("public class ").append(objClsName);
		if(isForm){
			sb.append("Form {").append("\n\n");
		}else if(isQuery){
			sb.append("Query implements IQuery {").append("\n\n");
		}else if(!isForm && !isQuery){
			sb.append("VO {").append("\n\n");
		}
		 
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			typeName = field.getType().toString();
			if(typeName.indexOf("[L") >= 0){
				typeName = typeName.split("L")[1].replace(";", "") + "[]";
			}else{
				typeName = typeName.indexOf(" ") > 0 ? typeName.split(" ")[1] : typeName;
			}
			
			if(!"serialVersionUID".equals(field.getName())){
				if(isForm && "id".equals(field.getName())){
					sb.append("\tprivate ").append(typeName).append(" ")
					  .append(str)
					  .append(field.getName().substring(0, 1).toUpperCase())
					  .append(field.getName().substring(1, field.getName().length())).append(";\n");
				}else if(isQuery){
					if(!"id".equals(field.getName())){
						sb.append("\tprivate ").append(typeName).append(" ").append(field.getName()).append(";\n");
					}
				}else{
					sb.append("\tprivate ").append(typeName).append(" ").append(field.getName()).append(";\n");
				}
			}
		}
		sb.append("\n");
		
		for (Field field : fields) {
			field.setAccessible(true);
			if(!"serialVersionUID".equals(field.getName())){
				typeName = field.getType().toString();
				if(typeName.indexOf("[L") >= 0){
					typeName = typeName.split("L")[1].replace(";", "") + "[]";
				}else{
					typeName = typeName.indexOf(" ") > 0 ? typeName.split(" ")[1] : typeName;
				}
				firstCharUpperFieldName = new StringBuilder().append(field.getName().substring(0, 1).toUpperCase())
															 .append(field.getName().substring(1, field.getName().length())).toString();
				
				if(isForm && "id".equals(field.getName())){
					sb.append("\tpublic ").append(typeName).append(" get").append(objClsName).append(firstCharUpperFieldName).append("() {\n")
					  .append("\t\treturn ").append(str).append(field.getName().substring(0, 1).toUpperCase())
					  .append(field.getName().substring(1, field.getName().length())).append(";\n\t}\n\n")
					  .append("\tpublic void set").append(objClsName).append(firstCharUpperFieldName).append("(").append(typeName).append(" ")
					  .append(str).append(field.getName().substring(0, 1).toUpperCase())
					  .append(field.getName().substring(1, field.getName().length())).append(") {\n")
					  .append("\t\tthis.").append(str).append(field.getName().substring(0, 1).toUpperCase())
					  .append(field.getName().substring(1, field.getName().length())).append(" = ").append(str)
					  .append(field.getName().substring(0, 1).toUpperCase())
					  .append(field.getName().substring(1, field.getName().length())).append(";\n\t}\n\n");
				}else if(isQuery){
					if(!"id".equals(field.getName())){
						sb.append("\tpublic ").append(typeName).append(" get").append(firstCharUpperFieldName).append("() {\n")
						  .append("\t\treturn ").append(field.getName()).append(";\n\t}\n\n")
						  .append("\tpublic void set").append(firstCharUpperFieldName).append("(").append(typeName).append(" ").append(field.getName()).append(") {\n")
						  .append("\t\tthis.").append(field.getName()).append(" = ").append(field.getName()).append(";\n\t}\n\n");
					}
				}else{
					if(typeName.indexOf("Date") > 0){
						sb.append("\t@JsonSerialize(using = JsonDatetimeSerializer.class)\n");
					}
					sb.append("\tpublic ").append(typeName).append(" get").append(firstCharUpperFieldName).append("() {\n")
					  .append("\t\treturn ").append(field.getName()).append(";\n\t}\n\n")
					  .append("\tpublic void set").append(firstCharUpperFieldName).append("(").append(typeName).append(" ").append(field.getName()).append(") {\n")
					  .append("\t\tthis.").append(field.getName()).append(" = ").append(field.getName()).append(";\n\t}\n\n");
				}
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 建一個Dao
	 * @param objClsName
	 * @param b
	 * @return
	 */
	private String daoGenerator(String objClsName) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("package ").append(DAO_PACKAGE_NAME).append(";\n\n")
		  .append("import com.cms.dao.BaseDao;\n\n")
		  .append("public interface ").append(objClsName).append("Dao extends BaseDao {\n\n")
		  .append("}");
		return sb.toString();
	}
	
	private String serviceGenerator(String objClsName) {
		StringBuilder sb = new StringBuilder();
		String str = objClsName.replaceFirst(String.valueOf(objClsName.charAt(0)), String.valueOf(objClsName.charAt(0)).toLowerCase());
		sb.append("package ").append(SERVICE_PACKAGE_NAME).append(";\n\n")
		  .append("import java.util.ArrayList;\n")
		  .append("import java.util.List;\n")
		  .append("import org.springframework.beans.BeanUtils;\n")
		  .append("import org.springframework.beans.factory.annotation.Autowired;\n")
		  .append("import org.springframework.stereotype.Service;\n")
		  .append("import com.cms.entity.").append(objClsName).append(";\n")
		  .append("import com.cms.dao.MybatisCriteria;\n")
		  .append("import com.cms.dao.").append(objClsName).append("Dao;\n")
		  .append("import com.cms.utils.BeanConvertUtil;\n")
		  .append("import com.cms.vo.").append(objClsName).append("VO;\n")
		  .append("import com.cms.vo.Json;\n");
		if(IS_NEED_COMBOBOX){
			sb.append("import com.cms.easyui.EasyuiCombobox;\n");
		}
		sb.append("import com.cms.easyui.EasyuiDatagrid;\n")
		  .append("import com.cms.easyui.EasyuiDatagridPager;\n")
		  .append("import com.cms.vo.form.").append(objClsName).append("Form;\n")
	      .append("import com.cms.query.").append(objClsName).append("Query;\n\n")
	      .append("@Service(\"").append(str).append("Service\")\n")
	      .append("public class ").append(objClsName).append("Service extends BaseService {\n\n")
	      .append("\t@Autowired\n")
	      .append("\tprivate ").append(objClsName).append("Dao ").append(str).append("Dao;\n\n")
	      .append("\tpublic EasyuiDatagrid<").append(objClsName).append("VO> getPagedDatagrid(EasyuiDatagridPager pager, ").append(objClsName).append("Query query) {\n")
	      .append("\t\tEasyuiDatagrid<").append(objClsName).append("VO> datagrid = new EasyuiDatagrid<").append(objClsName).append("VO>();\n")
	      .append("\t\tMybatisCriteria mybatisCriteria = new MybatisCriteria();\n")
	      .append("\t\tmybatisCriteria.setCurrentPage(pager.getPage());\n")
	      .append("\t\tmybatisCriteria.setPageSize(pager.getRows());\n")
	      .append("\t\tmybatisCriteria.setCondition(BeanConvertUtil.bean2Map(query));\n")
	      .append("\t\tList<").append(objClsName).append("> ").append(str).append("List = ").append(str).append("Dao.queryByPageList(mybatisCriteria);\n")
	      .append("\t\t").append(objClsName).append("VO ").append(str).append("VO = null;\n")
	      .append("\t\tList<").append(objClsName).append("VO> ").append(str).append("VOList = new ArrayList<").append(objClsName).append("VO>();\n")
	      .append("\t\tfor (").append(objClsName).append(" ").append(str).append(" : ").append(str).append("List) {\n")
	      .append("\t\t\t").append(str).append("VO = new ").append(objClsName).append("VO();\n")
	      .append("\t\t\tBeanUtils.copyProperties(").append(str).append(", ").append(str).append("VO);\n")
	      .append("\t\t\t").append(str).append("VOList.add(").append(str).append("VO);\n")
	      .append("\t\t}\n")
	      .append("\t\tdatagrid.setTotal((long) ").append(str).append("Dao.queryByCount(mybatisCriteria));\n")
	      .append("\t\tdatagrid.setRows(").append(str).append("VOList);\n")
	      .append("\t\treturn datagrid;\n")
	      .append("\t}\n\n")
	      .append("\tpublic Json add").append(objClsName).append("(").append(objClsName).append("Form ").append(str).append("Form) throws Exception {\n")
	      .append("\t\tJson json = new Json();\n")
	      .append("\t\t").append(objClsName).append(" ").append(str).append(" = new ").append(objClsName).append("();\n")
	      .append("\t\tBeanUtils.copyProperties(").append(str).append("Form, ").append(str).append(");\n")
	      .append("\t\t").append(str).append("Dao.add(").append(str).append(");\n")
	      .append("\t\tjson.setSuccess(true);\n")
	      .append("\t\treturn json;\n")
	      .append("\t}\n\n")
	      .append("\tpublic Json edit").append(objClsName).append("(").append(objClsName).append("Form ").append(str).append("Form) {\n")
	      .append("\t\tJson json = new Json();\n")
	      .append("\t\t").append(objClsName).append("Query ").append(str).append("Query = new ").append(objClsName).append("Query();\n")
	      .append("\t\t").append(str).append("Query.set").append(objClsName).append("Id(").append(str).append("Form.get").append(objClsName).append("Id());\n")
	      .append("\t\t").append(objClsName).append(" ").append(str).append(" = ").append(str).append("Dao.queryById(").append(str).append("Query);\n")
	      .append("\t\tBeanUtils.copyProperties(").append(str).append("Form, ").append(str).append(");\n")
	      .append("\t\t").append(str).append("Dao.update(").append(str).append(");\n")
	      .append("\t\tjson.setSuccess(true);\n")
	      .append("\t\treturn json;\n")
	      .append("\t}\n\n")
	      .append("\tpublic Json delete").append(objClsName).append("(String id) {\n")
	      .append("\t\tJson json = new Json();\n")
	      .append("\t\t").append(objClsName).append("Query ").append(str).append("Query = new ").append(objClsName).append("Query();\n")
	      .append("\t\t").append(str).append("Query.set").append(objClsName).append("Id(id);\n")
	      .append("\t\t").append(objClsName).append(" ").append(str).append(" = ").append(str).append("Dao.queryById(").append(str).append("Query);\n")
	      .append("\t\tif(").append(str).append(" != null){\n")
	      .append("\t\t\t").append(str).append("Dao.delete(").append(str).append(");\n")
	      .append("\t\t}\n")
	      .append("\t\tjson.setSuccess(true);\n")
	      .append("\t\treturn json;\n")
	      .append("\t}\n\n");
		
		if(IS_NEED_COMBOBOX){
			sb.append("\tpublic List<EasyuiCombobox> get").append(objClsName).append("Combobox() {\n")
		      .append("\t\tList<EasyuiCombobox> comboboxList = new ArrayList<EasyuiCombobox>();\n")
		      .append("\t\tEasyuiCombobox combobox = null;\n")
		      .append("\t\tList<").append(objClsName).append("> ").append(str).append("List = ").append(str).append("Dao.queryByAll();\n")
		      .append("\t\tif(").append(str).append("List != null && ").append(str).append("List.size() > 0){\n")
		      .append("\t\t\tfor(").append(objClsName).append(" ").append(str).append(" : ").append(str).append("List){\n")
		      .append("\t\t\t\tcombobox = new EasyuiCombobox();\n")
		      .append("\t\t\t\tcombobox.setId(String.valueOf(").append(str).append(".getId()));\n")
		      .append("\t\t\t\tcombobox.setValue(").append(str).append(".get").append(objClsName).append("Name());\n")
		      .append("\t\t\t\tcomboboxList.add(combobox);\n")
		      .append("\t\t\t}\n")
		      .append("\t\t}\n")
		      .append("\t\treturn comboboxList;\n")
		      .append("\t}\n\n");
		}
	    
	    sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 建一个controller
	 * @param objClsName
	 * @return
	 */
	private String controllerGenerator(String objClsName) {
		StringBuilder sb = new StringBuilder();
		String str = objClsName.replaceFirst(String.valueOf(objClsName.charAt(0)), String.valueOf(objClsName.charAt(0)).toLowerCase());
		sb.append("package ").append(CONTROLLER_PACKAGE_NAME).append(";\n\n")
		  .append("import java.util.HashMap;\n");
		if(IS_NEED_COMBOBOX){
			sb.append("import java.util.List;\n");
		}
		sb.append("import java.util.Map;\n")
		  .append("import javax.servlet.http.HttpSession;\n")
		  .append("import org.springframework.beans.factory.annotation.Autowired;\n")
		  .append("import org.springframework.stereotype.Controller;\n")
		  .append("import org.springframework.web.bind.annotation.RequestMapping;\n")
		  .append("import org.springframework.web.bind.annotation.ResponseBody;\n")
		  .append("import org.springframework.web.servlet.ModelAndView;\n")
		  .append("import com.cms.entity.UserLogin;\n")
		  .append("import com.cms.service.").append(objClsName).append("Service;\n")
		  .append("import com.cms.utils.ResourceUtil;\n")
		  .append("import com.cms.utils.annotation.Login;\n")
		  .append("import com.cms.vo.Json;\n")
		  .append("import com.cms.vo.").append(objClsName).append("VO;\n");
		if(IS_NEED_COMBOBOX){
			sb.append("import com.cms.easyui.EasyuiCombobox;\n");
		}
		sb.append("import com.cms.easyui.EasyuiDatagrid;\n")
		  .append("import com.cms.easyui.EasyuiDatagridPager;\n")
		  .append("import com.cms.vo.form.").append(objClsName).append("Form;\n")
		  .append("import com.cms.query.").append(objClsName).append("Query;\n\n")
		  .append("@Controller\n")
		  .append("@RequestMapping(\"").append(str).append("Controller\")\n")
		  .append("public class ").append(objClsName).append("Controller {\n\n")
		  .append("\t@Autowired\n")
		  .append("\tprivate ").append(objClsName).append("Service ").append(str).append("Service;\n\n")
		  .append("\t@Login\n")
		  .append("\t@RequestMapping(params = \"toMain\")\n")
		  .append("\tpublic ModelAndView toMain(String menuId) {\n")
		  .append("\t\tMap<String, Object> model = new HashMap<String, Object>();\n")
		  .append("\t\tmodel.put(\"menuId\", menuId);\n")
		  .append("\t\treturn new ModelAndView(\"").append(str).append("/main\", model);\n")
		  .append("\t}\n\n")
		  .append("\t@Login\n")
		  .append("\t@RequestMapping(params = \"showDatagrid\")\n")
		  .append("\t@ResponseBody\n")
		  .append("\tpublic EasyuiDatagrid<").append(objClsName).append("VO> showDatagrid(EasyuiDatagridPager pager, ").append(objClsName).append("Query query) {\n")
		  .append("\t\treturn ").append(str).append("Service.getPagedDatagrid(pager, query);\n")
		  .append("\t}\n\n")
		  .append("\t@Login\n")
		  .append("\t@RequestMapping(params = \"add\")\n")
		  .append("\t@ResponseBody\n")
		  .append("\tpublic Json add(").append(objClsName).append("Form ").append(str).append("Form) throws Exception {\n")
		  .append("\t\tJson json = ").append(str).append("Service.add").append(objClsName).append("(").append(str).append("Form);\n")
		  .append("\t\tif(json == null){\n")
		  .append("\t\t\tjson = new Json();\n")
		  .append("\t\t\tjson.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));\n")
		  .append("\t\t}\n")
		  .append("\t\treturn json;\n")
		  .append("\t}\n\n")
		  .append("\t@Login\n")
		  .append("\t@RequestMapping(params = \"edit\")\n")
		  .append("\t@ResponseBody\n")
		  .append("\tpublic Json edit(").append(objClsName).append("Form ").append(str).append("Form) throws Exception {\n")
		  .append("\t\tJson json = ").append(str).append("Service.edit").append(objClsName).append("(").append(str).append("Form);\n")
		  .append("\t\tif(json == null){\n")
		  .append("\t\t\tjson = new Json();\n")
		  .append("\t\t\tjson.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));\n")
		  .append("\t\t}\n")
		  .append("\t\treturn json;\n")
		  .append("\t}\n\n")
		  .append("\t@Login\n")
		  .append("\t@RequestMapping(params = \"delete\")\n")
		  .append("\t@ResponseBody\n")
		  .append("\tpublic Json delete(String id) {\n")
		  .append("\t\tJson json = ").append(str).append("Service.delete").append(objClsName).append("(id);\n")
		  .append("\t\tif(json == null){\n")
		  .append("\t\t\tjson = new Json();\n")
		  .append("\t\t\tjson.setMsg(ResourceUtil.getProcessResultMsg(json.isSuccess()));\n")
		  .append("\t\t}\n")
		  .append("\t\treturn json;\n")
		  .append("\t}\n\n")
		  .append("\t@Login\n")
		  .append("\t@RequestMapping(params = \"getBtn\")\n")
		  .append("\t@ResponseBody\n")
		  .append("\tpublic Json getBtn(String id, HttpSession session) {\n")
		  .append("\t\treturn ").append(str).append("Service.getBtn(id, (UserLogin)session.getAttribute(ResourceUtil.getUserInfo()));\n")
		  .append("\t}\n\n");
		  
		if(IS_NEED_COMBOBOX){
			sb.append("\t@Login\n")
			  .append("\t@RequestMapping(params = \"getCombobox\")\n")
			  .append("\t@ResponseBody\n")
			  .append("\tpublic List<EasyuiCombobox> getCombobox() {\n")
			  .append("\t\treturn ").append(str).append("Service.get").append(objClsName).append("Combobox();\n")
			  .append("\t}\n\n");
		}
		  
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 建立一个JSP
	 * 
	 * @param objClsName
	 * @return
	 */
	private String jspGenerator(String objClsName) {
		StringBuilder sb = new StringBuilder();
		Field[] fields = obj.getClass().getDeclaredFields();
		String str = objClsName.replaceFirst(String.valueOf(objClsName.charAt(0)), String.valueOf(objClsName.charAt(0)).toLowerCase());
		sb.append("<%@ page language='java' pageEncoding='UTF-8'%>\n")
		  .append("<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>\n")
		  .append("<%@ taglib uri='http://www.springframework.org/tags' prefix='spring'%>\n")
		  .append("<!DOCTYPE html>\n")
		  .append("<html>\n")
		  .append("<head>\n")
		  .append("<c:import url='/WEB-INF/jsp/include/meta.jsp' />\n")
		  .append("<c:import url='/WEB-INF/jsp/include/easyui.jsp' />\n")
		  .append("<script type='text/javascript'>\n")
		  .append("var processType;\n")
		  .append("var ezuiMenu;\n")
		  .append("var ezuiForm;\n")
		  .append("var ezuiDialog;\n")
		  .append("var ezuiDatagrid;\n")
		  .append("$(function() {\n")
		  .append("\tezuiMenu = $('#ezuiMenu').menu();\n")
		  .append("\tezuiForm = $('#ezuiForm').form();\n")
		  .append("\tezuiDatagrid = $('#ezuiDatagrid').datagrid({\n")
		  .append("\t\turl : '<c:url value=\"/").append(str).append("Controller.do?showDatagrid\"/>',\n")
		  .append("\t\tmethod:'POST',\n")
		  .append("\t\ttoolbar : '#toolbar',\n")
		  .append("\t\ttitle: '待输入标题',\n")
		  .append("\t\tpageSize : 50,\n")
		  .append("\t\tpageList : [50, 100, 200],\n")
		  .append("\t\tfit: true,\n")
		  .append("\t\tborder: false,\n")
		  .append("\t\tfitColumns : true,\n")
		  .append("\t\tnowrap: true,\n") 
		  .append("\t\tstriped: true,\n")
		  .append("\t\tcollapsible:false,\n")
		  .append("\t\tpagination:true,\n")
		  .append("\t\trownumbers:true,\n")
		  .append("\t\tsingleSelect:true,\n")
		  .append("\t\tidField : 'id',\n")
		  .append("\t\tcolumns : [[\n");
		  
		int i = 0;
		for(Field field : fields){
			if(!"id".equals(field.getName()) &&!"serialVersionUID".equals(field.getName())){
			  sb.append("\t\t\t{field: '").append(field.getName()).append("',\t\ttitle: '待输入栏位").append(i).append("',\twidth: ").append(800/fields.length).append(" },\n");
			  i++;
			}
		}		  
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("\t\t]],\n")
		  .append("\t\tonDblClickCell: function(index,field,value){\n")
		  .append("\t\t\tedit();\n")
		  .append("\t\t},\n")
		  .append("\t\tonRowContextMenu : function(event, rowIndex, rowData) {\n")
		  .append("\t\t\tevent.preventDefault();\n")
		  .append("\t\t\t$(this).datagrid('unselectAll');\n")
		  .append("\t\t\t$(this).datagrid('selectRow', rowIndex);\n")
		  .append("\t\t\tezuiMenu.menu('show', {\n")
		  .append("\t\t\t\tleft : event.pageX,\n")
		  .append("\t\t\t\ttop : event.pageY\n")
		  .append("\t\t\t});\n")
		  .append("\t\t},onLoadSuccess:function(data){\n")
		  .append("\t\t\tajaxBtn($('#menuId').val(), '<c:url value=\"/").append(str).append("Controller.do?getBtn\"/>', ezuiMenu);\n")
		  .append("\t\t\t$(this).datagrid('unselectAll');\n")
		  .append("\t\t}\n")
		  .append("\t});\n")
		  .append("\tezuiDialog = $('#ezuiDialog').dialog({\n")
		  .append("\t\tmodal : true,\n")
		  .append("\t\ttitle : '<spring:message code=\"common.dialog.title\"/>',\n")
		  .append("\t\tbuttons : '#ezuiDialogBtn',\n")
		  .append("\t\tonClose : function() {\n")
		  .append("\t\t\tezuiFormClear(ezuiForm);\n")
		  .append("\t\t}\n")
		  .append("\t}).dialog('close');\n")
		  .append("});\n")
		  .append("var add = function(){\n")
		  .append("\tprocessType = 'add';\n")
		  .append("\t$('#").append(str).append("Id').val(0);\n")
		  .append("\tezuiDialog.dialog('open');\n")
		  .append("};\n")
		  .append("var edit = function(){\n")
		  .append("\tprocessType = 'edit';\n")
		  .append("\tvar row = ezuiDatagrid.datagrid('getSelected');\n")
		  .append("\tif(row){\n")
		  .append("\t\tezuiForm.form('load',{\n");
		  for(Field field : fields){	
			  if(!"serialVersionUID".equals(field.getName())){
				  if("id".equals(field.getName())){
					  sb.append("\t\t\t").append(str).append("Id : row.id,\n");
				  }else{
					  sb.append("\t\t\t").append(field.getName()).append(" : row.").append(field.getName()).append(",\n");
				  }
			  }
		  }
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("\t\t});\n")
		  .append("\t\tezuiDialog.dialog('open');\n")
		  .append("\t}else{\n")
		  .append("\t\t$.messager.show({\n")
		  .append("\t\t\tmsg : '<spring:message code=\"common.message.selectRecord\"/>', title : '<spring:message code=\"common.message.prompt\"/>'\n")
		  .append("\t\t});\n")
		  .append("\t}\n")
		  .append("};\n")
		  .append("var del = function(){\n")
		  .append("\tvar row = ezuiDatagrid.datagrid('getSelected');\n")
		  .append("\tif(row){\n")
		  .append("\t\t$.messager.confirm('<spring:message code=\"common.message.confirm\"/>', '<spring:message code=\"common.message.confirm.delete\"/>', function(confirm) {\n")
		  .append("\t\t\tif(confirm){\n")
		  .append("\t\t\t\t$.ajax({\n")
		  .append("\t\t\t\t\turl : '").append(str).append("Controller.do?delete',\n")
		  .append("\t\t\t\t\tdata : {id : row.id},\n")
		  .append("\t\t\t\t\ttype : 'POST',\n")
		  .append("\t\t\t\t\tdataType : 'JSON',\n")
		  .append("\t\t\t\t\tsuccess : function(result){\n")
		  .append("\t\t\t\t\t\tvar msg = '';\n")
		  .append("\t\t\t\t\t\ttry {\n")
		  .append("\t\t\t\t\t\t\tmsg = result.msg;\n")
		  .append("\t\t\t\t\t\t} catch (e) {\n")
		  .append("\t\t\t\t\t\t\tmsg = '<spring:message code=\"common.message.data.delete.failed\"/>';\n")
		  .append("\t\t\t\t\t\t} finally {\n")
		  .append("\t\t\t\t\t\t\t$.messager.show({\n")
		  .append("\t\t\t\t\t\t\t\tmsg : msg, title : '<spring:message code=\"common.message.prompt\"/>'\n")
		  .append("\t\t\t\t\t\t\t});\n")
		  .append("\t\t\t\t\t\t\tezuiDatagrid.datagrid('reload');\n")
		  .append("\t\t\t\t\t\t}\n")
		  .append("\t\t\t\t\t}\n")
		  .append("\t\t\t\t});\n")
		  .append("\t\t\t}\n")
		  .append("\t\t});\n")
		  .append("\t}else{\n")
		  .append("\t\t$.messager.show({\n")
		  .append("\t\t\tmsg : '<spring:message code=\"common.message.selectRecord\"/>', title : '<spring:message code=\"common.message.prompt\"/>'\n")
		  .append("\t\t});\n")
		  .append("\t}\n")
		  .append("};\n")
		  .append("var commit = function(){\n")
		  .append("\tvar url = '';\n")
		  .append("\tif (processType == 'edit') {\n")
		  .append("\t\turl = '<c:url value=\"/").append(str).append("Controller.do?edit\"/>';\n")
		  .append("\t}else{\n")
		  .append("\t\turl = '<c:url value=\"/").append(str).append("Controller.do?add\"/>';\n")
		  .append("\t}\n")
		  .append("\tezuiForm.form('submit', {\n")
		  .append("\t\turl : url,\n")
		  .append("\t\tonSubmit : function(){\n")
		  .append("\t\t\tif(ezuiForm.form('validate')){\n")
		  .append("\t\t\t\t$.messager.progress({\n")
		  .append("\t\t\t\t\ttext : '<spring:message code=\"common.message.data.processing\"/>', interval : 100\n")
		  .append("\t\t\t\t});\n")
		  .append("\t\t\t\treturn true;\n")
		  .append("\t\t\t}else{\n")
		  .append("\t\t\t\treturn false;\n")
		  .append("\t\t\t}\n")
		  .append("\t\t},\n")
		  .append("\t\tsuccess : function(data) {\n")
		  .append("\t\t\tvar msg='';\n")
		  .append("\t\t\ttry {\n")
		  .append("\t\t\t\tvar result = $.parseJSON(data);\n")
		  .append("\t\t\t\tif(result.success){\n")
		  .append("\t\t\t\t\tmsg = result.msg;\n")
		  .append("\t\t\t\t\tezuiDatagrid.datagrid('reload');\n")
		  .append("\t\t\t\t\tezuiDialog.dialog('close');\n")
		  .append("\t\t\t\t}else{\n")
		  .append("\t\t\t\t\tmsg = '<font color=\"red\">' + result.msg + '</font>';\n")
		  .append("\t\t\t\t}\n")
		  .append("\t\t\t} catch (e) {\n")
		  .append("\t\t\t\tmsg = '<font color=\"red\">' + JSON.stringify(data).split('description')[1].split('</u>')[0].split('<u>')[1] + '</font>';\n")
		  .append("\t\t\t\tmsg = '<spring:message code=\"common.message.data.process.failed\"/><br/>'+ msg;\n")
		  .append("\t\t\t} finally {\n")
		  .append("\t\t\t\t$.messager.show({\n")
		  .append("\t\t\t\t\tmsg : msg, title : '<spring:message code=\"common.message.prompt\"/>'\n")
		  .append("\t\t\t\t});\n")
		  .append("\t\t\t\t$.messager.progress('close');\n")
		  .append("\t\t\t}\n")
		  .append("\t\t}\n")
		  .append("\t});\n")
		  .append("};\n")
		  .append("var doSearch = function(){\n")
		  .append("\tezuiDatagrid.datagrid('load', {\n");
		i = 0;
		for(Field field : fields){
			if(!"id".equals(field.getName()) &&!"serialVersionUID".equals(field.getName())){
				sb.append("\t\t").append(field.getName()).append(" : $('#").append(field.getName()).append("').val(),\n");
				i++;
			}
		}	
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("\t});\n")
		  .append("};\n")
		  .append("</script>\n")
		  .append("</head>\n")
		  .append("<body>\n")
		  .append("\t<input type='hidden' id='menuId' name='menuId' value='${menuId}'/>\n")
		  .append("\t<div class='easyui-layout' data-options='fit:true,border:false'>\n")
		  .append("\t\t<div data-options='region:\"center\",border:false' style='overflow: hidden;'>\n")
		  .append("\t\t\t<div id='toolbar' class='datagrid-toolbar' style='padding: 5px;'>\n")
		  .append("\t\t\t\t<fieldset>\n")
		  .append("\t\t\t\t\t<legend><spring:message code='common.legend.query'/></legend>\n")
		  .append("\t\t\t\t\t<table>\n")
		  .append("\t\t\t\t\t\t<tr>\n");
		i = 0;
		for(Field field : fields){
			if(!"id".equals(field.getName()) &&!"serialVersionUID".equals(field.getName())){
				sb.append("\t\t\t\t\t\t\t<th>待输入名称").append(i).append("：</th><td><input type='text' id='").append(field.getName()).append("' class='easyui-textbox' size='16' data-options=''/></td>\n");
			}
			i++;
		}			  
		sb.append("\t\t\t\t\t\t\t<td>\n")
		  .append("\t\t\t\t\t\t\t\t<a onclick='doSearch();' class='easyui-linkbutton' data-options='plain:true,iconCls:\"icon-search\"' href='javascript:void(0);'>查詢</a>\n")
		  .append("\t\t\t\t\t\t\t\t<a onclick='ezuiToolbarClear(\"#toolbar\");' class='easyui-linkbutton' data-options='plain:true,iconCls:\"icon-remove\"' href='javascript:void(0);'><spring:message code='common.button.clear'/></a>\n")
		  .append("\t\t\t\t\t\t\t</td>\n")
		  .append("\t\t\t\t\t\t</tr>\n")
		  .append("\t\t\t\t\t</table>\n")
		  .append("\t\t\t\t</fieldset>\n")
		  .append("\t\t\t\t<div>\n")
		  .append("\t\t\t\t\t<a onclick='add();' id='ezuiBtn_add' class='easyui-linkbutton' data-options='plain:true,iconCls:\"icon-add\"' href='javascript:void(0);'><spring:message code='common.button.add'/></a>\n")
		  .append("\t\t\t\t\t<a onclick='del();' id='ezuiBtn_del' class='easyui-linkbutton' data-options='plain:true,iconCls:\"icon-remove\"' href='javascript:void(0);'><spring:message code='common.button.delete'/></a>\n")
		  .append("\t\t\t\t\t<a onclick='edit();' id='ezuiBtn_edit' class='easyui-linkbutton' data-options='plain:true,iconCls:\"icon-edit\"' href='javascript:void(0);'><spring:message code='common.button.edit'/></a>\n")
		  .append("\t\t\t\t\t<a onclick='clearDatagridSelected(\"#ezuiDatagrid\");' class='easyui-linkbutton' data-options='plain:true,iconCls:\"icon-undo\"' href='javascript:void(0);'><spring:message code='common.button.cancelSelect'/></a>\n")
		  .append("\t\t\t\t</div>\n")
		  .append("\t\t\t</div>\n")
		  .append("\t\t\t<table id='ezuiDatagrid'></table> \n")
		  .append("\t\t</div>\n")
		  .append("\t</div>\n")
		  .append("\t<div id='ezuiDialog' style='padding: 10px;'>\n")
		  .append("\t\t<form id='ezuiForm' method='post'>\n")
		  .append("\t\t\t<input type='hidden' id='").append(str).append("Id' name='").append(str).append("Id'/>\n")
		  .append("\t\t\t<table>\n");
		  
		i = 0;
		String typeName = null;
		for(Field field : fields){
			typeName = field.getType().toString();
			typeName = typeName.indexOf(" ") > 0 ? typeName.split(" ")[1] : typeName;
			if(!"id".equals(field.getName()) &&!"serialVersionUID".equals(field.getName())){
				sb.append("\t\t\t\t<tr>\n")
				  .append("\t\t\t\t\t<th>待输入").append(i).append("</th>\n");
				if(typeName.indexOf("Integer") > 0 || 
						typeName.indexOf("Double") > 0 || 
							typeName.indexOf("Long") > 0 ){
					sb.append("\t\t\t\t\t<td><input type='text' name='").append(field.getName()).append("' class='easyui-numberbox' size='16' data-options='required:true,min:0,max:100'/></td>\n")
					  .append("\t\t\t\t</tr>\n");
				}else{
					sb.append("\t\t\t\t\t<td><input type='text' name='").append(field.getName()).append("' class='easyui-textbox' size='16' data-options='required:true'/></td>\n")
					  .append("\t\t\t\t</tr>\n");
				}
				
				i++;
			}
		}

		sb.append("\t\t\t</table>\n")
		  .append("\t\t</form>\n")
		  .append("\t</div>\n")
		  .append("\t<div id='ezuiDialogBtn'>\n")
		  .append("\t\t<a onclick='commit();' id='ezuiBtn_commit' class='easyui-linkbutton' href='javascript:void(0);'><spring:message code='common.button.commit'/></a>\n")
		  .append("\t\t<a onclick='ezuiDialogClose(\"#ezuiDialog\");' class='easyui-linkbutton' href='javascript:void(0);'><spring:message code='common.button.close'/></a>\n")
		  .append("\t</div>\n")
		  .append("\t<div id='ezuiMenu' class='easyui-menu' style='width:120px;display: none;'>\n")
		  .append("\t\t<div onclick='add();' id='menu_add' data-options='plain:true,iconCls:\"icon-add\"'><spring:message code='common.button.add'/></div>\n")
		  .append("\t\t<div onclick='del();' id='menu_del' data-options='plain:true,iconCls:\"icon-remove\"'><spring:message code='common.button.delete'/></div>\n")
		  .append("\t\t<div onclick='edit();' id='menu_edit' data-options='plain:true,iconCls:\"icon-edit\"'><spring:message code='common.button.edit'/></div>\n")
		  .append("\t</div>\n")
		  .append("</body>\n")
		  .append("</html>\n");
		
		return sb.toString();
	}
}
