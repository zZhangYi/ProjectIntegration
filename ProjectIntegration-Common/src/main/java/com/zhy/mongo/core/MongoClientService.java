package com.zhy.mongo.core;

import com.zhy.mongo.dto.MongoEntity;

import java.util.List;

/**
 * 【类或接口功能描述】 MongoDB 客户端接口
 *
 * @version 1.0.0
 */
public interface MongoClientService {

    /**
     * 保存或更新
     *
     * @param mongoEntity    实体类
     * @param collectionName 集合名【表名】
     */
    void save(MongoEntity<?> mongoEntity, String collectionName);

    /**
     * 查询
     *
     * @param bizNo          业务唯一号
     * @param obj            对象
     * @param collectionName 集合名【表名】
     * @return
     */
    List<?> findByBizNo(String bizNo, Object obj, String collectionName);

}
