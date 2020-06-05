package com.zhy.mongo.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.zhy.enums.SystemTypeEnum;
import com.zhy.mongo.core.MongoClientService;
import com.zhy.mongo.dto.MongoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 【类或接口功能描述】
 * 异步保存日志订阅者
 * @version 1.0.0
 */
@Component
public class SaveMsgEventSubscribe {

    private static final Logger logger = LoggerFactory.getLogger(SaveMsgEventSubscribe.class);

    private static final String SEP_SPOT = ".";

    @Autowired
    private MongoClientService mongoClientService;

    @Subscribe
    @AllowConcurrentEvents
    public void saveData(AsynSaveMsgDTO asynSaveMsgDTO) {
        if (logger.isDebugEnabled()) {
            logger.debug("asynSaveMsgDTO.getData()={}", asynSaveMsgDTO.getData());
        }
        MongoEntity<?> mongoEntity = new MongoEntity<>(asynSaveMsgDTO.getSeqNo(), asynSaveMsgDTO.getBizNo(), asynSaveMsgDTO.getMsgTypeEnum().name(),
            asynSaveMsgDTO.getData());
        if (logger.isDebugEnabled()) {
            logger.debug("mongoEntity.getDatas()={}", mongoEntity.getDatas());
        }
        String collectionName = initCollectionName(asynSaveMsgDTO.getSystemTypeEnum(), asynSaveMsgDTO.getBusinessType());
        mongoClientService.save(mongoEntity, collectionName);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void dealDead(DeadEvent deadEvent) {
        logger.warn("Async Message To MongoDB Exception,{}", deadEvent);
    }

    /**
     * 组装集合名称
     *
     * @param sysTypeEnum 系统类型
     * @param bizType 业务名称
     * @return 集合名称
     */
    private String initCollectionName(SystemTypeEnum sysTypeEnum, String bizType) {
        return sysTypeEnum.getValue() + SEP_SPOT + bizType;
    }

}
