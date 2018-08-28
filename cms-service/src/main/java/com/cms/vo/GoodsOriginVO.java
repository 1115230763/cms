package com.cms.vo;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.cms.utils.serialzer.JsonDatetimeSerializer;

public class GoodsOriginVO {

	private java.lang.Integer goodsId;
	private java.lang.String goodsName;
	private java.lang.Integer goodsType;
	private java.lang.String goodsOrigin;
	private java.lang.String goodsStandard;
	private java.lang.Integer purchaseType;
	private java.lang.String goodsStorage;
	private java.lang.String storageType;
	private java.lang.String goodsImage;
	private java.lang.String goodsDesc;
	private java.lang.Integer expiryDate;
	private java.lang.Integer status;
	private java.util.Date createDate;
	private java.util.Date updateDate;
	private String name;
	private String value;
	private String purchaseTypeName;
	
	
	public String getPurchaseTypeName() {
		return purchaseTypeName;
	}

	public void setPurchaseTypeName(String purchaseTypeName) {
		this.purchaseTypeName = purchaseTypeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public java.lang.Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(java.lang.Integer goodsId) {
		this.goodsId = goodsId;
	}

	public java.lang.String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(java.lang.String goodsName) {
		this.goodsName = goodsName;
	}

	public java.lang.Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(java.lang.Integer goodsType) {
		this.goodsType = goodsType;
	}

	public java.lang.String getGoodsOrigin() {
		return goodsOrigin;
	}

	public void setGoodsOrigin(java.lang.String goodsOrigin) {
		this.goodsOrigin = goodsOrigin;
	}

	public java.lang.String getGoodsStandard() {
		return goodsStandard;
	}

	public void setGoodsStandard(java.lang.String goodsStandard) {
		this.goodsStandard = goodsStandard;
	}

	public java.lang.Integer getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(java.lang.Integer purchaseType) {
		this.purchaseType = purchaseType;
	}

	public java.lang.String getGoodsStorage() {
		return goodsStorage;
	}

	public void setGoodsStorage(java.lang.String goodsStorage) {
		this.goodsStorage = goodsStorage;
	}

	public java.lang.String getStorageType() {
		return storageType;
	}

	public void setStorageType(java.lang.String storageType) {
		this.storageType = storageType;
	}

	public java.lang.String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(java.lang.String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public java.lang.String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(java.lang.String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	
	public java.lang.Integer getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(java.lang.Integer expiryDate) {
		this.expiryDate = expiryDate;
	}

	public java.lang.Integer getStatus() {
		return status;
	}

	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}

	@JsonSerialize(using = JsonDatetimeSerializer.class)
	public java.util.Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}

	@JsonSerialize(using = JsonDatetimeSerializer.class)
	public java.util.Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	

}