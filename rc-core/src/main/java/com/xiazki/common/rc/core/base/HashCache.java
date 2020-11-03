package com.xiazki.common.rc.core.base;

import java.util.List;
import java.util.Map;

public interface HashCache<K, HK, HV> {

    HV get(K key, HK hk);

    List<HV> listHValue(K key, List<HK> hks);

    void put(K key, HK hk, HV hv);

    void putAll(K key, Map<HK,HV> map);

    List<HV> values(K key);

    Map<HK, HV> valuesMap(K key);

    void delHKey(K key, HK hk);

    void delHKeys(K key, List<HK> hks);

    List<HV> multiGet(List<K> keys, List<HK> hks);

}
