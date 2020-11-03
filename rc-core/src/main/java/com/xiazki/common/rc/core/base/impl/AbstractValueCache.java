package com.xiazki.common.rc.core.base.impl;


import com.xiazki.common.rc.core.base.ValueCache;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class AbstractValueCache<K, V> extends AbstractBaseCache<K, V> implements ValueCache<K, V> {

    @Override
    public V get(K key) {
        RedisTemplate<String, V> redisTemplate = getRedisTemplate();
        return redisTemplate.opsForValue().get(buildKey(key));
    }

    @Override
    public void set(K key, V value) {
        getRedisTemplate().opsForValue().set(buildKey(key), value, getTTL(), TimeUnit.MILLISECONDS);
    }

    @Override
    public List<V> multiGet(List<K> keys) {
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> fullNameKeys = keys.stream().map(this::buildKey).collect(Collectors.toList());
        List<V> vs = getRedisTemplate().opsForValue().multiGet(fullNameKeys);
        if (vs == null) {
            return new ArrayList<>();
        }
        return vs.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void multiSet(Map<K, V> map) {
        if (map == null) {
            return;
        }
        multiSetExpire(map, getTTL());
    }

    @Override
    public void multiSetExpire(Map<K, V> map, Long time) {
        Map<String, V> valueMap = new HashMap<>();
        map.forEach((k, v) -> valueMap.put(buildKey(k), v));
        getRedisTemplate().executePipelined(new SessionCallback<Object>() {
            @SuppressWarnings({"unchecked", "NullableProblems"})
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().multiSet(valueMap);
                valueMap.keySet().forEach(k -> operations.expire(k, time == null ? getTTL() : time, TimeUnit.MILLISECONDS));
                return null;
            }
        });
    }

    /**
     * 返回keys中不存在的key
     * <p>
     * 流水线批量判断是否存在key
     *
     * @param keys
     * @return
     */
    @Override
    public List<K> batchHasKeyIfAbsent(List<K> keys) {
        List<K> missKeyList = new ArrayList<>();
        List<Object> objects = getRedisTemplate().executePipelined(new SessionCallback<Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public Object execute(@SuppressWarnings("NullableProblems") RedisOperations operations) throws DataAccessException {
                keys.forEach(key -> {
                    operations.hasKey(buildKey(key));
                });
                return null;
            }
        });
        for (int i = 0; i < keys.size(); i++) {
            if (!(Boolean) objects.get(i)){
                missKeyList.add(keys.get(i));
            }
        }
        return missKeyList;
    }

}