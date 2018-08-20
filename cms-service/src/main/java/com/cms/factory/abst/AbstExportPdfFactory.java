package com.cms.factory.abst;

import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import com.cms.product.abst.AbstPdfExport;
import com.cms.vo.form.ExportPdfForm;

public abstract class AbstExportPdfFactory {
	public enum CreatePdfTypeEnum{
		STOWAGE_ALL_IN_ONE;
	}
	
	private void doExportPdf(HttpServletResponse response, AbstPdfExport export, ExportPdfForm form, PDFMergerUtility pdfMergerUtility) throws Exception {
		export.exportData(response, form, export.createResponseData(response, form), pdfMergerUtility);
	}
	
	public void doExportPdf(HttpServletResponse response, ExportPdfForm form, CreatePdfTypeEnum createPdfTypeEnum) throws Exception{
		doExportPdf(response, chooseCreatePdf(createPdfTypeEnum), form, null);
    }

	protected abstract AbstPdfExport chooseCreatePdf(CreatePdfTypeEnum createPdfType);
}
