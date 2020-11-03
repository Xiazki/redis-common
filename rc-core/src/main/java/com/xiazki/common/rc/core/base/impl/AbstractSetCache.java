package com.xiazki.common.rc.core.base.impl;

import com.xiazki.common.rc.core.base.SetCache;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSetCache<K, V> extends AbstractBaseCache<K, V> implements SetCache<K, V> {

    @Override
    public void add(K key, V... sets) {
        getRedisTemplate().opsForSet().add(buildKey(key), sets);
        if (getTTL() != -1L) {
            getRedisTemplate().expire(buildKey(key), getTTL(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public Long size(K key) {
        return getRedisTemplate().opsForSet().size(buildKey(key));
    }

    @Override
    public Boolean isMember(K key, V value) {
        return getRedisTemplate().opsForSet().isMember(buildKey(key), value);
    }

    @Override
    public Set<V> members(K key) {
        return getRedisTemplate().opsForSet().members(buildKey(key));
    }

}
