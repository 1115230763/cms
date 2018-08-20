package com.cms.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class BatchBeanFieldSetMapper implements FieldSetMapper<BatchBean> {

	@Override
	public BatchBean mapFieldSet(FieldSet fieldSet) throws BindException {
		BatchBean batchBean = new BatchBean();
//		batchBean.setId(fieldSet.readInt(0));
//		batchBean.setSales(fieldSet.readBigDecimal(1));
//		batchBean.setQty(fieldSet.readInt(2));
//		batchBean.setStaffName(fieldSet.readString(3));
// 
//		String date = fieldSet.readString(4);
//		try {
//			batchBean.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		batchBean.setName(fieldSet.readString(0));
		batchBean.setCredit(fieldSet.readString(1));
		return batchBean;
	}

}
