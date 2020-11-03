package com.xiazki.common.rc.core.base;

import org.springframework.data.redis.core.RedisTemplate;

public interface RedisTemplateAware<V> {

    void setRedisTemplate(RedisTemplate<String,V> redisTemplate);

    RedisTemplate<String,V> getRedisTemplate();

}