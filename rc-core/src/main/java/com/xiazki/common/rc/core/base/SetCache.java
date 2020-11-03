package com.xiazki.common.rc.core.base;

import java.util.Set;

public interface SetCache<K,V> {


    void add(K key, V... sets);

    Long size(K key);

    Boolean isMember(K key, V value);

    Set<V> members(K key);

}
