package com.cms.product.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

import com.itextpdf.text.pdf.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cms.product.abst.AbstPdfExport;
import com.cms.utils.BarcodeGeneratorUtil;
import com.cms.utils.DateUtil;
import com.cms.utils.PDFUtil;
import com.cms.utils.ResourceUtil;
import com.cms.vo.form.ExportPdfForm;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;

public class StowageAllInOnePDF extends AbstPdfExport {
	
	@Override
	protected boolean isDoExport(ExportPdfForm form) {
		return true;
	}

	@Override
	protected List<InputStream> prepareData(ExportPdfForm form) throws Exception {
		List<InputStream> inputStreamList = new ArrayList<>();
		
		InputStream stowageOrderInputStream = getStowageOrder(form);
		if(stowageOrderInputStream != null && stowageOrderInputStream.available() > 0){
			inputStreamList.add(stowageOrderInputStream);
		}
		
		return inputStreamList;
	}
	
	private static final String HEAD = "序号,订单号,客户简称,门店名称,联系方式,箱数,体积,重量,库位,订单备注";
	
	/**
	 * 配载明细表
	 * @param form
	 * @return
	 * @throws Exception 
	 */
	private InputStream getStowageOrder(ExportPdfForm form) throws Exception {
		
		int index = 1;
		
		StringBuilder sb = new StringBuilder();
		List<String> dataList = new ArrayList<String>();
		ByteArrayOutputStream dataBaos = new ByteArrayOutputStream();
		
		Document document = new Document(PageSize.A4, 1, 1, 5, 5);
		PdfPTable table = new PdfPTable(new float[] {6, 12, 10, 26, 12, 6, 7, 7, 7, 12});
		Font chFont = new Font(BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 9);
		Font titleFont = new Font(BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 12);
		    
        PdfWriter.getInstance(document, dataBaos);
        document.open();
        
		for(int i = 0; i < 8; i++){
			sb.append(index++).append(",")
			  .append("123456");
			sb.append(",")
			  .append("123456").append(",");
			sb.append("9999").append(",");
			sb.append("888888").append(",");
			sb.append("0").append(",");
			sb.append("0").append(",");
			sb.append("0").append(",");
			sb.append("0").append(",");
			sb.append("999999999999999");
			dataList.add(sb.toString());
			sb.setLength(0);
		}
		
		if(dataList != null && dataList.size() > 0){
			insertCell(table, "司机", Element.ALIGN_CENTER, 2, titleFont);
			insertCell(table, "???????????", Element.ALIGN_CENTER, 4, titleFont);
			insertCell(table, "出库库区", Element.ALIGN_CENTER, 2, titleFont);
			insertCell(table, "???????????", Element.ALIGN_CENTER, 2, titleFont);
			
			insertCell(table, "发车时间", Element.ALIGN_CENTER, 2, titleFont);
			insertCell(table, "???????????", Element.ALIGN_CENTER, 4, titleFont);
			insertCell(table, "", Element.ALIGN_CENTER, 4, titleFont);
			insertCell(table, "配载单明细", Element.ALIGN_CENTER, 10, titleFont);
			for(String data : HEAD.split(",")){
				insertCell(table, data, Element.ALIGN_LEFT, 1, chFont);
			}
			
			for(String dataStr : dataList){
				for(String data : dataStr.split(",")){
					insertCell(table, data, Element.ALIGN_LEFT, 1, chFont);
				}
	        }
		}
        document.add(table);
        document.close();
        return new ByteArrayInputStream(dataBaos.toByteArray());
	}

	@Override
	protected void prepareData(PdfCopy pdfCopy, ExportPdfForm form) throws Exception {}
}
