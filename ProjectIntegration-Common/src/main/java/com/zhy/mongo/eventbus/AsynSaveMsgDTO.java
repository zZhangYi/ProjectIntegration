package com.zhy.mongo.eventbus;

import com.zhy.enums.SystemTypeEnum;
import com.zhy.mongo.enums.MsgTypeEnum;

/**
 * 【类或接口功能描述】
 * 异步保存日志DTO
 *
 * @version 1.0.0
 */
public class AsynSaveMsgDTO {

    /**
     * 系统类型
     */
    private SystemTypeEnum systemTypeEnum;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务流水号，系统内唯一，由各系统自行生成
     */
    private String seqNo;

    /**
     * 需要保存的报文对象
     */
    private Object data;

    /**
     * 报文类型
     */
    private MsgTypeEnum msgTypeEnum;
    /**
     * 业务号
     */
    private String bizNo;


    public SystemTypeEnum getSystemTypeEnum() {
        return systemTypeEnum;
    }

    public void setSystemTypeEnum(SystemTypeEnum systemTypeEnum) {
        this.systemTypeEnum = systemTypeEnum;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public MsgTypeEnum getMsgTypeEnum() {
        return msgTypeEnum;
    }

    public void setMsgTypeEnum(MsgTypeEnum msgTypeEnum) {
        this.msgTypeEnum = msgTypeEnum;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }
}
