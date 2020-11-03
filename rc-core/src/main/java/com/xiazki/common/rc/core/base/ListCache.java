package com.xiazki.common.rc.core.base;


import java.util.List;
import java.util.Map;

public interface ListCache<K, V> {

    void add(K key, V value);

    void addList(K key, List<V> values);

    long size(K key);

    /**
     * 会取出全部list，可能会出现大对象，使用时注意
     *
     * @param key
     * @return
     */
    List<V> listAll(K key);

    List<V> list(K key, int offset, int size);

    V leftPop(K key);


    List<V> leftPop(K key, int size);

    Long rightPushIfPresent(K key,V value);

    /**
     * 会取出全部list，可能会出现大对象，使用时注意
     *
     * @param keys
     * @return
     */
    List<V> listKeysAll(List<K> keys);

    /**
     * 会取出全部list，转成map,可能会出现大对象，使用时注意
     *
     * @param keys
     * @return
     */
    Map<K, List<V>> listKeysAllMap(List<K> keys);

}
