package com.example.notificationgenerator.repository;

import com.example.notificationgenerator.entity.CustomSubscription;
import com.example.notificationgenerator.entity.ThresholdSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Service
public class ThresholdInMemoryCache {

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(cacheNames = "threshold", key = "#sub.id")
    public ThresholdSubscription insert(ThresholdSubscription sub){
        return sub;
    }

    @CachePut(cacheNames = "threshold", key = "#sub.id")
    public ThresholdSubscription update(ThresholdSubscription sub){
        return sub;
    }

    public ThresholdSubscription get(int id){
        return cacheManager.getCache("custom").get(id, ThresholdSubscription.class);
    }

    public Map<Integer, ThresholdSubscription> getCache(){
        Object nativeCache = cacheManager.getCache("custom").getNativeCache();
        if (nativeCache instanceof ConcurrentMap) {
            ConcurrentMap<Integer, ThresholdSubscription> map = (ConcurrentMap<Integer, ThresholdSubscription>) nativeCache;
            return new HashMap<>(map);} else {
            return null;
        }
    }


}
