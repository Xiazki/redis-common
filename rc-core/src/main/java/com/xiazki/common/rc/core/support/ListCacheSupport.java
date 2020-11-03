package com.xiazki.common.rc.core.support;

import com.xiazki.common.rc.core.base.impl.AbstractListCache;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ListCacheSupport {


    public static <K, V> List<V> listAll(K key, AbstractListCache<K, V> listCache, Function<K, List<V>> sourceFunction) {
        //1、先拿缓存
        List<V> retList = listCache.listAll(key);
        if (retList != null && !retList.isEmpty()) {
            return retList;
        }
        //2、缓存未命中，去取数据
        List<V> sourceList = sourceFunction.apply(key);
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }
        //3、设置缓存
        listCache.addList(key, sourceList);
        return sourceList;
    }

    public static <K, V> List<V> listKeysAll(List<K> key, AbstractListCache<K, V> listCache, Function<K, List<V>> sourceFunction) {
        List<V> vs = listCache.listKeysAll(key);

        return vs;
    }

}