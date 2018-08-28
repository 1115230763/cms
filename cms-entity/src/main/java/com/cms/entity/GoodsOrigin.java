package com.cms.entity;

import java.io.Serializable;
import java.util.Date;

public class GoodsOrigin implements Serializable {
    private Integer goodsId;

    private String goodsName;

    private Integer goodsType;

    private String goodsOrigin;

    private String goodsStandard;

    private Integer purchaseType;

    private String goodsStorage;

    private String storageType;

    private String goodsImage;

    private String goodsDesc;

    private Integer expiryDate;

    private Integer status;

    private Date createDate;

    private Date updateDate;
    
    private String name;
    
    private String value;
    
    private String createBy;
    
    private String updateBy;
    
    private String purchaseTypeName;
    
    
    public String getPurchaseTypeName() {
		return purchaseTypeName;
	}

	public void setPurchaseTypeName(String purchaseTypeName) {
		this.purchaseTypeName = purchaseTypeName;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsOrigin() {
        return goodsOrigin;
    }

    public void setGoodsOrigin(String goodsOrigin) {
        this.goodsOrigin = goodsOrigin == null ? null : goodsOrigin.trim();
    }

    public String getGoodsStandard() {
        return goodsStandard;
    }

    public void setGoodsStandard(String goodsStandard) {
        this.goodsStandard = goodsStandard == null ? null : goodsStandard.trim();
    }

    public Integer getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(Integer purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(String goodsStorage) {
        this.goodsStorage = goodsStorage == null ? null : goodsStorage.trim();
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType == null ? null : storageType.trim();
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage == null ? null : goodsImage.trim();
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc == null ? null : goodsDesc.trim();
    }

    

    public Integer getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Integer expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
    
}