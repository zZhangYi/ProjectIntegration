package com.zhy.mongo.core;

import com.zhy.mongo.dto.MongoEntity;
import com.zhy.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 【类或接口功能描述】 MongoDB 客户端实现类
 *
 * @version 1.0.0
 */
@Component("mongoClientService")
public class MongoClientServiceImpl implements MongoClientService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存或更新
     *
     * @param mongoEntity    实体类
     * @param collectionName 集合名【表名】
     */
    @Override
    public void save(MongoEntity<?> mongoEntity, String collectionName) {
        mongoEntity.setCreatedDate(DateUtil.getCurrentDate(DateUtil.ZH_CN_DATETIME_PATTERN));
        mongoTemplate.save(mongoEntity, collectionName);
    }

    /**
     * 查询
     *
     * @param bizNo          业务唯一号
     * @param obj            对象
     * @param collectionName 集合名【表名】
     * @return 集合列表
     */
    @Override
    public List<?> findByBizNo(String bizNo, Object obj, String collectionName) {
        Query query = Query.query(Criteria.where("bizNo").is(bizNo));
        return mongoTemplate.find(query, obj.getClass(), collectionName);
    }

}
