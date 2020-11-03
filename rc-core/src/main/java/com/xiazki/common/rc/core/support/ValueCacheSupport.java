package com.xiazki.common.rc.core.support;


import com.xiazki.common.rc.core.base.ValueCache;

import java.util.*;
import java.util.function.Function;

public final class ValueCacheSupport {

    /**
     * 包装 get 先重缓存中拿，没有再从数据库拿
     */
    public static <K, V> V get(K key, ValueCache<K, V> valueCache, Function<K, V> sourceFunc) {
        V v = valueCache.get(key);
        if (v != null) {
            return v;
        }
        V res = sourceFunc.apply(key);
        if (res != null) {
            valueCache.set(key, res);
        }
        return res;
    }

    /**
     * 批量获取缓存
     * <p>
     * 使用此方法需要缓存对象继承{@link CacheKeyWrapper}
     *
     * @param keys       缓存keys
     * @param valueCache 缓存
     * @param sourceFunc 缓存来源函数
     * @param <K>        缓存key
     * @param <V>        缓存对象
     * @return List<V>
     */
    @SuppressWarnings("unchecked")
    public static <K, V extends CacheKeyWrapper> List<V> multiGet(List<K> keys, ValueCache<K, V> valueCache, Function<List<K>, List<V>> sourceFunc) {
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }
        List<V> vs = valueCache.multiGet(keys);
        if (Objects.equals(keys.size(), vs.size())) {
            return vs;
        }
        List<K> missKeyList = valueCache.batchHasKeyIfAbsent(keys);
        if (missKeyList.isEmpty()) {
            return vs;
        }
        List<V> reGetList = sourceFunc.apply(missKeyList);
        vs.addAll(reGetList);
        Map<K, V> storeMap = new HashMap<>();
        reGetList.forEach(v -> {
            CacheKeyWrapper<K> cacheBaseDto = (CacheKeyWrapper<K>) v;
            storeMap.put(cacheBaseDto.getKey(), v);
        });
        valueCache.multiSet(storeMap);
        return vs;
    }
}