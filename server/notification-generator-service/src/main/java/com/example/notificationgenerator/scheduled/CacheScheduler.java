package com.example.notificationgenerator.scheduled;

import com.example.notificationgenerator.core.HazelcastNotificationGenerator;
import com.example.notificationgenerator.entity.ThresholdSubscription;
import com.example.notificationgenerator.repository.ThresholdInMemoryCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class CacheScheduler {

    @Autowired
    private ThresholdInMemoryCache thresholdInMemoryCache;
    @Scheduled(cron = "${in-memory.cache.scheduler.threshold.update.cron.expression}")
    public void updateThresholdInMemoryCache(){
        if(thresholdInMemoryCache.getCache() != null && thresholdInMemoryCache.getCache().size() > 0) {
            thresholdInMemoryCache.getCache().entrySet().parallelStream().filter(entry -> entry.getValue().dailyLimit() > 0).forEach(entry -> {
                ThresholdSubscription sub = entry.getValue();
                thresholdInMemoryCache.update(new ThresholdSubscription(sub.id(), sub.userId(), sub.stockId(), sub.price(), sub.threshold(), sub.type(), 0));
            });
        }
    }

    @Scheduled(cron = "${hazelcast.scheduler.delete.notified.cron.expression}")
    public void clearHazelcastNotified(){
        HazelcastNotificationGenerator.customNotified.clear();
        HazelcastNotificationGenerator.thresholdNotified.clear();
    }

}
