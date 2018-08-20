package com.cms.product.abst;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.cms.entity.enumerator.ContentTypeEnum;
import com.cms.utils.DateUtil;
import com.cms.utils.ExceptionUtil;
import com.cms.utils.PDFUtil;
import com.cms.vo.form.ExportPdfForm;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public abstract class AbstPdfExport extends SpringBeanAutowiringSupport {
	
	private static final Logger logger = Logger.getLogger(AbstPdfExport.class);
	
	/**
	 * 1.建立Response数据
	 * @throws Exception 
	 */
	public boolean createResponseData(HttpServletResponse response, ExportPdfForm form) {
		boolean isDoExport = this.isDoExport(form);
		if(isDoExport){
			response.setContentType(ContentTypeEnum.pdf.getContentType());
		}else{
			response.setContentType("text/html;charset=utf-8");
		}
		return isDoExport;
	}

	/**
	 * 2.Response输出PDF数据
	 * @param response
	 * @param form
	 * @throws Exception
	 */
	public void exportData(HttpServletResponse response, ExportPdfForm form, boolean isDoExport, PDFMergerUtility pdfMergerUtility) throws Exception {
		StringBuilder sb = new StringBuilder();
		if(isDoExport){
			sb.append("inline; filename=")
			  .append(URLEncoder.encode(
					  	new StringBuilder().append(form.getExportFilename()).append("_").append(DateUtil.format(new Date(), "yyyyMMddHHmmssSSS")).append(".pdf").toString(),
					  	"UTF-8"
					 ));
			response.setHeader("Content-disposition", sb.toString());
			try (OutputStream os = response.getOutputStream()) {
				System.out.println(this.prepareData(form));
				pdfMergerUtility.addSources(this.prepareData(form));
				pdfMergerUtility.setDestinationStream(os);
				pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
			} catch (Exception e) {
				logger.error(ExceptionUtil.getExceptionMessage(e));
			}
		}else{
			sb.append("<Script language=\"javaScript\">\r\n").append("alert(\"").append("查无数据，导出失败！").append("\");").append("</Script>");
			try (PrintWriter out = response.getWriter();) {
				out.println(sb.toString());
			} catch (Exception e) {
				logger.error(ExceptionUtil.getExceptionMessage(e));
			}
		}
	}

	/**
	 * 判断是否有數據可以生成PDF
	 * @param form
	 * @return
	 */
	protected abstract boolean isDoExport(ExportPdfForm form);

	/**
	 * 生成数据
	 * @param pdfCopy
	 * @param form
	 * @return 
	 * @throws Exception
	 */
	protected abstract void prepareData(PdfCopy pdfCopy, ExportPdfForm form) throws Exception;
	
	/**
	 * 生成数据 合并pdf输出
	 * @param form
	 * @return
	 * @throws Exception
	 */
	protected abstract List<InputStream> prepareData(ExportPdfForm form) throws Exception;
	
	protected void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
//		if (text.trim().equalsIgnoreCase("")) {
//			cell.setMinimumHeight(10f);
//		}
		table.addCell(cell);
	}
}
