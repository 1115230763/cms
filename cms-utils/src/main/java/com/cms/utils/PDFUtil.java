package com.cms.utils;

import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PushbuttonField;

/**
 * PDF 工具类
 */
public class PDFUtil {
	
	/**
	 * 取得指定档名的PDF范本
	 * @param templateName
	 * @return
	 * @throws Exception
	 */
	public static PdfReader getTemplate(String templateName) throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append(ResourceUtil.getPdfTemplateRootPath()).append(templateName);
		return new PdfReader(sb.toString());
	}
	
	/**
	 * 将button栏位填充指定图片
	 * @param form
	 * @param filedName
	 * @param img
	 * @return
	 * @throws Exception
	 */
	public static PdfFormField genPdfButton(AcroFields form, String filedName, byte[] img) throws Exception {
		PushbuttonField button = null;
		button = form.getNewPushbuttonFromField(filedName);
		button.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
		button.setProportionalIcon(false);
		button.setImage(Image.getInstance(img));
		return button.getField();
	}
	
	public static ArrayList<BaseFont> getFontList() throws DocumentException, IOException {
		ArrayList<BaseFont> fontList = new ArrayList<>();
		BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);  
		fontList.add(bfChinese);
		return fontList;
	}

}
