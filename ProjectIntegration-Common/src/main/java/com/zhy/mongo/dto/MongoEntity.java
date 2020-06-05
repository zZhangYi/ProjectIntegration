package com.zhy.mongo.dto;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 【类或接口功能描述】 MongoDB通用保存Entity
 *
 * @version 1.0.0
 */
public class MongoEntity<T extends Object> {

    /**
     * 业务唯一号
     */
    @Field("traceId")
    private String traceId;
    /**
     * 业务唯一号
     */
    @Field("bizNo")
    private String bizNo;

    /**
     * 业务类型
     */
    @Field("bizType")
    private String bizType;
    /**
     * 待存储数据
     */
    @Field("datas")
    private T      datas;

    /**
     * 创建时间
     */
    @Field("createdDate")
    private String createdDate;

    public MongoEntity(String traceId, String bizNo, String bizType, T datas) {
        this.traceId = traceId;
        this.bizNo = bizNo;
        this.bizType = bizType;
        this.datas = datas;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
