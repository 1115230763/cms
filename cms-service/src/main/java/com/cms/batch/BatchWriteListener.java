package com.cms.batch;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ItemWriteListener;

import com.cms.utils.ExceptionUtil;

public class BatchWriteListener implements ItemWriteListener<BatchBean> {

	private static final Logger logger = Logger.getLogger(BatchWriteListener.class);
	
	@Override
	public void beforeWrite(List<? extends BatchBean> batchBeanList) {
		System.out.println("ItemWriteListener - beforeWrite");
	}
	
	@Override
	public void afterWrite(List<? extends BatchBean> batchBeanList) {
		System.out.println("ItemWriteListener - afterWrite");
	}

	@Override
	public void onWriteError(Exception exception, List<? extends BatchBean> batchBeanList) {
		logger.error(ExceptionUtil.getExceptionMessage(exception));
	}
}
