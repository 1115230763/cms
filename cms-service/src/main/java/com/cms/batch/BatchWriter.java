package com.cms.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.cms.service.UserService;
 
public class BatchWriter implements ItemWriter<BatchBean>{

	@Autowired
	private UserService adminUserService;
	
	@Override
	public void write(List<? extends BatchBean> batchBeans) {
		for(BatchBean batchBean : batchBeans){
			System.out.println(batchBean.getName());
			if(batchBean.getName().equals("S200206395")){
				throw new RuntimeException();
			}
//			else{
//				AdminUser adminUser = new AdminUser();
//				adminUser.setId(batchBean.getName());
//				try {
//					adminUserService.addAdminUser(adminUser);
//				} catch (Exception e) {
//					throw new RuntimeException();
//				}
//			}
		}
		System.out.println("共"+batchBeans.size()+"资料处理完毕");
	}

}
