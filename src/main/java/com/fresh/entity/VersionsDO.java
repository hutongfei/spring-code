package com.fresh.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Versions)实体类
 *
 * @author hutf
 * @since 2021-09-06 14:28:35
 */
public class VersionsDO implements Serializable {
    private static final long serialVersionUID = 199501654693195294L;
    
    private String id;
    /**
    * 1安卓 2 ios
    */
    private Integer type;
    /**
    * 下载地址
    */
    private String apkUrl;
    /**
    * 版本号 例如：101
    */
    private Object versionNum;
    /**
    * 版本号标识：例如：1.0.1
    */
    private String versionCode;
    /**
    * 备注
    */
    private String remarks;
    
    private Date createTime;
    
    private Integer state;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public Object getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Object versionNum) {
        this.versionNum = versionNum;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

}