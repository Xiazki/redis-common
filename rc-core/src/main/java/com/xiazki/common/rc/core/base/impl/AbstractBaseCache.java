package com.xiazki.common.rc.core.base.impl;


import com.xiazki.common.rc.core.base.BaseCache;
import org.springframework.data.redis.core.RedisTemplate;

public abstract class AbstractBaseCache<K, V> implements BaseCache<K, V> {

    private RedisTemplate<String, V> redisTemplate;

    /**
     * key的前缀，返回值非空
     *
     * @return no-null string prefix
     */
    protected abstract String getPrefix();

    protected String buildKey(K key) {
        if (null == key) {
            return getPrefix();
        }
        return getPrefix() + key.toString();
    }

    @Override
    public void del(K key) {
        getRedisTemplate().delete(buildKey(key));
    }

    @Override
    public Boolean exist(K key) {
        return getRedisTemplate().hasKey(buildKey(key));
    }

    /**
     * 默认是7天
     * 单位 毫秒
     *
     * @return 缓存失效时间
     */
    @Override
    public Long getTTL() {
        return 7 * 24 * 3600 * 1000L;
    }

    @Override
    public void setRedisTemplate(RedisTemplate<String, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisTemplate<String, V> getRedisTemplate() {
        return redisTemplate;
    }
}
