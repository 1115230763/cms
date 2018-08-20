package com.cms.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * 公用条件查询类
 */
public class MybatisCriteria {
	/**
	 * 存放条件查询值
	 */
	private Map<String, Object> condition;
	/**
	 * 存放分页条件
	 */
	private int currentPage = 1;
	private int totalCount = 0;
	private int pageSize = 10;
	/**
	 * 是否相异
	 */
	protected boolean distinct;
	/**
	 * 排序字段
	 */
	protected String orderByClause;
	/**
	 * 分页字段
	 */
	protected String limit;
	/**
	 * 分页字段
	 */
	protected String limitClause;

	protected MybatisCriteria(MybatisCriteria example) {
		this.orderByClause = example.orderByClause;
		this.condition = example.condition;
		this.distinct = example.distinct;
		this.limitClause = example.limitClause;
	}

	public MybatisCriteria() {
		condition = new HashMap<String, Object>();
	}

	public void clear() {
		condition.clear();
		distinct = false;
		orderByClause = null;
		limit = null;
		limitClause = null;
	}

	public MybatisCriteria put(String condition, Object value) {
		this.condition.put(condition, value);
		return (MybatisCriteria) this;
	}

	public void setCondition(Map<String, Object> condition) {
		this.condition = condition;
	}

	public Map<String, Object> getCondition() {
		return condition;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public String getLimit() { 
		if(currentPage==0 && pageSize==0){
			return null;
		}else{
			int minRow = (this.currentPage - 1) * this.pageSize;
			limit = " LIMIT " + minRow + " , " + this.pageSize;
			return limit;
		}
	}

	public String getLimitClause() {
		int minRow = (this.currentPage - 1) * this.pageSize + 1;
		int maxRow = this.currentPage * this.pageSize;
		limitClause = " and row_num between " + minRow + " and " + maxRow;
		return limitClause;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * 得到总行数
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 得到总页数
	 * 
	 * @return
	 */
	public int getTotalPage() {
		int pageCount = this.totalCount / this.pageSize + 1;
		// 如果模板==0，且总数大于1，则减一
		if ((this.totalCount % this.pageSize == 0) && pageCount > 1)
			pageCount--;
		return pageCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}