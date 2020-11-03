package com.xiazki.common.rc.core.base;

import java.util.List;
import java.util.Map;

public interface ValueCache<K, V> {

    V get(K key);

    void set(K key, V value);

    List<V> multiGet(List<K> keys);

    void multiSet(Map<K, V> map);

    void multiSetExpire(Map<K,V> map,Long time);

    List<K> batchHasKeyIfAbsent(List<K> keys);
}
