package com.cms.batch;

import org.springframework.batch.item.ItemProcessor;

public class BatchProcessor implements ItemProcessor<BatchBean, BatchBean> {

	@Override
	public BatchBean process(BatchBean batchBean) throws Exception {
		System.out.println("执行中..."+batchBean);
		return batchBean;
	}

}
