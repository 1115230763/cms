package com.cms.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ItemReadListener;

import com.cms.utils.ExceptionUtil;

public class BatchReadListener implements ItemReadListener<BatchBean> {
	
	private static final Logger logger = Logger.getLogger(BatchReadListener.class);
	
	@Override
	public void beforeRead() {
		System.out.println("ItemReadListener - beforeRead");
	}
	
	@Override
	public void afterRead(BatchBean batchBean) {
		System.out.println("ItemReadListener - afterRead");
	}

	@Override
	public void onReadError(Exception exception) {
		logger.error(ExceptionUtil.getExceptionMessage(exception));
	}
}
