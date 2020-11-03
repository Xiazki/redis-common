package com.xiazki.common.rc.core.base.impl;

import com.xiazki.common.rc.core.base.ListCache;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class AbstractListCache<K, V> extends AbstractBaseCache<K, V> implements ListCache<K, V> {

    @Override
    public void add(K key, V value) {
        getRedisTemplate().opsForList().rightPush(buildKey(key), value);
        getRedisTemplate().expire(buildKey(key), getTTL(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void addList(K key, List<V> values) {
        getRedisTemplate().opsForList().rightPushAll(buildKey(key), values);
        getRedisTemplate().expire(buildKey(key), getTTL(), TimeUnit.MILLISECONDS);
    }

    @Override
    public long size(K key) {
        Long size = getRedisTemplate().opsForList().size(buildKey(key));
        return size == null ? 0 : size;
    }

    @Override
    public List<V> listAll(K key) {
        Long size = getRedisTemplate().opsForList().size(buildKey(key));
        if (size != null && size != 0) {
            return getRedisTemplate().opsForList().range(buildKey(key), 0, size);
        }
        return new ArrayList<>();
    }

    @Override
    public List<V> list(K key, int offset, int size) {
        return null;
    }

    @Override
    public V leftPop(K key) {
        return getRedisTemplate().opsForList().leftPop(buildKey(key));
    }

    @Override
    public Long rightPushIfPresent(K key, V value) {
        return getRedisTemplate().opsForList().rightPushIfPresent(buildKey(key),value);
    }

    @Override
    public List<V> leftPop(K key, int size) {
        List<Object> objects = getRedisTemplate().executePipelined(new SessionCallback<Object>() {
            @SuppressWarnings({"unchecked", "NullableProblems"})
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (int i = 0; i < size; i++) {
                    operations.opsForList().leftPop(buildKey(key));
                }
                return null;
            }
        });
        //noinspection unchecked
        return objects.stream().map(o -> (V) o).collect(Collectors.toList());
    }

    @Override
    public List<V> listKeysAll(List<K> keys) {
        List<V> retList = new ArrayList<>();
        List<Object> objects = getRedisTemplate().executePipelined(new SessionCallback<Object>() {
            @SuppressWarnings({"NullableProblems", "unchecked"})
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                keys.forEach(k -> {
                    List range = operations.opsForList().range(buildKey(k), 0, -1);
                });
                return null;
            }
        });
        return (List<V>) objects;
    }

    public Map<K, List<V>> listKeysAllMap(List<K> keys) {
        Map<K, List<V>> retMap = new HashMap<>();
        List<Object> objects = getRedisTemplate().executePipelined(new SessionCallback<Object>() {
            @SuppressWarnings({"NullableProblems", "unchecked"})
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                keys.forEach(k -> {
                    operations.opsForList().range(buildKey(k), 0, -1);
                });
                return null;
            }
        });
        for (int i = 0;i<keys.size();i++){
            List o = (List) objects.get(i);
            if (o!=null && !o.isEmpty()){
                retMap.put(keys.get(i),o);
            }
        }
        return retMap;
    }
}
