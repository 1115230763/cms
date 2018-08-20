package com.cms.utils;

import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 	freemaker建立word共用工具類
 */
public class FreemakerUtil {

	/**
	 * 取得模版
	 * @return
	 * @throws Exception
	 */
	private static Template createTemplate(String templateName) throws Exception {
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		configuration.setClassForTemplateLoading(FreemakerUtil.class, ResourceUtil.getWordTemplateRootPath());
		
		Template template = configuration.getTemplate(templateName);
		template.setEncoding("utf-8");
		
		return template;
	}
	
	/**
	 * 产生word
	 * @param templateName
	 * @param vo
	 * @param out
	 * @throws Exception
	 */
	public static void generator(String templateName, Object vo, Writer out) throws Exception {
		Template template = FreemakerUtil.createTemplate(templateName);
		template.process(vo, out);
	}
}
