package com.cms.factory.pdf;

import com.cms.factory.abst.AbstExportPdfFactory;
import com.cms.product.abst.AbstPdfExport;
import com.cms.product.pdf.StowageAllInOnePDF;

public class ServiceExportPdfFactory extends AbstExportPdfFactory {

	@Override
	protected AbstPdfExport chooseCreatePdf(CreatePdfTypeEnum createPdfType) {
		AbstPdfExport export = null;
		switch (createPdfType) {
			case STOWAGE_ALL_IN_ONE: 		export = new StowageAllInOnePDF(); 		break;
			default:break;
		}
		return export;
	}

}
