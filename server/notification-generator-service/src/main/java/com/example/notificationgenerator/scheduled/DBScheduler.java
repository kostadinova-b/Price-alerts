package com.example.notificationgenerator.scheduled;

import com.example.notificationgenerator.repository.CustomSubRepository;
import com.example.notificationgenerator.repository.ThresholdSubRepository;
import com.example.notificationgenerator.repository.entity.PriceUpdateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class DBScheduler {
    public static ConcurrentLinkedDeque<PriceUpdateEntity> prices = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<Integer> customSubIds = new ConcurrentLinkedDeque<>();
    @Autowired
    private ThresholdSubRepository thresholdSubRepository;
    @Autowired
    private CustomSubRepository customSubRepository;

    @Value("${db.scheduler.batch.size}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${db.scheduler.delete.custom.delay}")
    public void deleteCustomNotified(){
        if(customSubIds.size() > batchSize){
            customSubRepository.batchDelete(customSubIds.stream().toList());
            customSubIds.clear();
        }
    }

    @Scheduled(fixedDelayString = "${db.scheduler.update.threshold.delay}")
    public void updateThresholdNotifications(){
        if(prices.size() > batchSize) {
            thresholdSubRepository.batchPriceUpdate(prices.stream().toList());
            prices.clear();
        }
    }
}
