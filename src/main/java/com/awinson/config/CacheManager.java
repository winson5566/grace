package com.awinson.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by winson on 2016/12/7.
 */

public class CacheManager {
    private static Map<String, Object> caches;

    private CacheManager() {}

    static {
        caches = new HashMap<String, Object>();
    }

    //用于保存缓存
    public static void update(String key, Object value) {
        caches.put(key, value);
    }

    //用于得到缓存
    public static Object get(String key) {
        return caches.get(key);
    }

    //用于清除缓存信息
    public static void clear() {
        caches.clear();
    }

    //用于清除指定的缓存信息
    public static void remove(String key) {
        caches.remove(key);
    }

    //获取所有缓存
    public static Map<String, Object> getCaches() {
        return caches;
    }

    //获取指定类型的缓存
    public static Map<String, Object> getCachesByType(String type) {
        Map<String, Object> allMap =  getCaches();
        Map<String,Object> cloneMap = new HashMap();
        if (allMap!=null&&allMap.size()>0){
            for (Map.Entry<String,Object> map:allMap.entrySet()) {
                if (map.getKey().matches("^"+type+"\\d+")){
                    cloneMap.put(map.getKey(),map.getValue());
                }
            }
        }
        return cloneMap;
    }




}