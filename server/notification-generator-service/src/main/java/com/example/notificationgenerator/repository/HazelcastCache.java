package com.example.notificationgenerator.repository;

import com.example.notificationgenerator.entity.CustomSubscription;
import com.example.notificationgenerator.entity.PriceType;
import com.example.notificationgenerator.entity.ThresholdSubscription;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.Map;

@Service
public class HazelcastCache {

    @Qualifier("hcastBean")
    @Autowired
    private HazelcastInstance hazelcastInstance;
    @Autowired
    private IMap<Integer, CustomSubscription> customMap;

    @Autowired
    private IMap<Integer, ThresholdSubscription> thresholdMap;

    public int countRec(){
        return customMap.size() + thresholdMap.size();
    }

    public Map<Integer, CustomSubscription> getCustomSubscriptions() {
        Set<Integer> keys = customMap.localKeySet();
        return customMap.getAll(keys);
    }

    public Map<Integer, ThresholdSubscription> getThresholdSubscriptions() {
        Set<Integer> keys = thresholdMap.localKeySet();
        return thresholdMap.getAll(keys);
    }

    public List<CustomSubscription> getCustomSubscriptionsByIds(Set<Integer> ids){
        return customMap.getAll(ids).values().stream().toList();
    }
    public void updateThreshold(Integer key, ThresholdSubscription sub) {
        thresholdMap.put(key, sub);
    }


    public void addCustom(Integer key, CustomSubscription sub) {
        customMap.put(key, sub);
    }

    public void addThreshold(Integer key, ThresholdSubscription sub) {
        thresholdMap.put(key, sub);
    }


    public List<ThresholdSubscription> getThresholdSubscriptionsByIds(Set<Integer> ids) {
        return thresholdMap.getAll(ids).values().stream().toList();
    }


}
