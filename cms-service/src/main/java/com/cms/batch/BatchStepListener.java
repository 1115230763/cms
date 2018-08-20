package com.cms.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class BatchStepListener implements StepExecutionListener {

	@Override
	public ExitStatus afterStep(StepExecution execution) {
		System.out.println("StepExecutionListener - afterStep");
		return null;
	}

	@Override
	public void beforeStep(StepExecution arg0) {
		System.out.println("StepExecutionListener - beforeStep");
	}

}
