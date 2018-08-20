package com.cms.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class BatchReader implements ItemReader<BatchBean> {

	@Override
	public BatchBean read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		BatchBean batchBean = new BatchBean();
		return batchBean;
	}
}
