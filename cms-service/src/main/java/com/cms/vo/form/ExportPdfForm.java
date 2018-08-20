package com.cms.vo.form;

public class ExportPdfForm {

	private Integer exportMethod;
	private String exportFilename;
	private String templateName;

	public Integer getExportMethod() {
		return exportMethod;
	}

	public void setExportMethod(Integer exportMethod) {
		this.exportMethod = exportMethod;
	}

	public String getExportFilename() {
		return exportFilename;
	}

	public void setExportFilename(String exportFilename) {
		this.exportFilename = exportFilename;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
}
