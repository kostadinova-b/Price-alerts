package com.example.notificationgenerator.repository;

import com.example.notificationgenerator.entity.CustomSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Service
public class CustomInMemoryCache {
    @Autowired
    private CacheManager cacheManager;

    @Cacheable(cacheNames = "custom", key = "#sub.id")
    public CustomSubscription insert(CustomSubscription sub){
        return sub;
    }

    @CacheEvict(cacheNames = "custom", key = "#key")
    public void delete(int key){

    }

    public CustomSubscription get(int key){
        return cacheManager.getCache("custom").get(key, CustomSubscription.class);
    }

    public Map<Integer, CustomSubscription> getCache(){
        Object nativeCache = cacheManager.getCache("custom").getNativeCache();
        if (nativeCache instanceof ConcurrentMap) {
            ConcurrentMap<Integer, CustomSubscription> map = (ConcurrentMap<Integer, CustomSubscription>) nativeCache;
        return new HashMap<>(map);} else {
            return null;
        }
    }
}
