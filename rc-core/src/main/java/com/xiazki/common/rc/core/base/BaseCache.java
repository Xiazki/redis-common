package com.xiazki.common.rc.core.base;

public interface BaseCache<K, V> extends RedisTemplateAware<V> {

    /**
     * 默认是7天
     * 单位 毫秒
     *
     * @return 缓存失效时间
     */
    Long getTTL();

    void del(K key);

    Boolean exist(K key);

}
