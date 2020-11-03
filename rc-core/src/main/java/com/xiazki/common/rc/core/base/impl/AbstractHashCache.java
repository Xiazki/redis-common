package com.xiazki.common.rc.core.base.impl;

import com.xiazki.common.rc.core.base.HashCache;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class AbstractHashCache<K, HK, HV> extends AbstractBaseCache<K, HV> implements HashCache<K, HK, HV> {

    @Override
    public HV get(K key, HK hk) {
        HashOperations<String, HK, HV> hash = getRedisTemplate().opsForHash();
        return hash.get(buildKey(key), hk);
    }

    @Override
    public List<HV> listHValue(K key, List<HK> hks) {
        HashOperations<String, HK, HV> hash = getRedisTemplate().opsForHash();
        List<HV> hvs = hash.multiGet(buildKey(key), hks);
        List<HV> retList = new ArrayList<>();
        //按照hks的顺序排序
        for (int i = 0; i < hks.size(); i++) {
            retList.add(hvs.get(i));
        }
        return retList;
    }

    @Override
    public void put(K key, HK hk, HV hv) {
        HashOperations<String, HK, HV> hash = getRedisTemplate().opsForHash();
        hash.put(buildKey(key), hk, hv);
        getRedisTemplate().expire(buildKey(key), getTTL(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void putAll(K key, Map<HK, HV> map) {
        HashOperations<String, HK, HV> hash = getRedisTemplate().opsForHash();
        hash.putAll(buildKey(key), map);
        getRedisTemplate().expire(buildKey(key), getTTL(), TimeUnit.MILLISECONDS);
    }

    @Override
    public List<HV> values(K key) {
        HashOperations<String, HK, HV> hash = getRedisTemplate().opsForHash();
        return hash.values(buildKey(key));
    }

    @Override
    public Map<HK, HV> valuesMap(K key) {
        HashOperations<String, HK, HV> hash = getRedisTemplate().opsForHash();
        return hash.entries(buildKey(key));
    }

    @Override
    public void delHKey(K key, HK hk) {
        delHKeys(key, Collections.singletonList(hk));
    }

    @Override
    public void delHKeys(K key, List<HK> hks) {
        HashOperations<String, HK, HV> hash = getRedisTemplate().opsForHash();
        hash.delete(buildKey(key), hks);
    }

    //todo
    @Override
    public List<HV> multiGet(List<K> keys, List<HK> hks) {
        if (hks == null || hks.isEmpty()) {
            return new ArrayList<>();
        }

        List<Object> objects = getRedisTemplate().executePipelined((RedisCallback<Object>) connection -> {


            return null;
        });

        return null;
    }

}
