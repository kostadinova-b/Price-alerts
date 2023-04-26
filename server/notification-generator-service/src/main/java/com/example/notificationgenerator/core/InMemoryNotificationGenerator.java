package com.example.notificationgenerator.core;

import com.example.notificationgenerator.dto.NotificationDto;
import com.example.notificationgenerator.dto.SubType;
import com.example.notificationgenerator.entity.CustomSubscription;
import com.example.notificationgenerator.entity.PriceType;
import com.example.notificationgenerator.entity.Stock;
import com.example.notificationgenerator.entity.ThresholdSubscription;
import com.example.notificationgenerator.repository.CustomInMemoryCache;
import com.example.notificationgenerator.repository.ThresholdInMemoryCache;
import com.example.notificationgenerator.repository.entity.PriceUpdateEntity;
import com.example.notificationgenerator.scheduled.DBScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InMemoryNotificationGenerator {
    private final double EPSILON = 10e-5;
    @Autowired
    private CustomInMemoryCache customInMemoryCache;
    @Autowired
    private ThresholdInMemoryCache thresholdInMemoryCache;


    public List<NotificationDto> generateCustom(Stock stock) {
        List<NotificationDto> notifications = new ArrayList<>();
        Map<Integer, CustomSubscription> map = customInMemoryCache.getCache();
        List<Integer> subIds = map.entrySet().parallelStream().filter(entry -> entry.getValue().stockId() == stock.id() && (entry.getValue().type().equals(PriceType.BUY) && compare(entry.getValue().price(), stock.buy()) == 0 ||
                entry.getValue().type().equals(PriceType.SELL) && compare(entry.getValue().price(), stock.sell()) == 0)).map(entry -> entry.getKey()).toList();
        if (subIds.size() == 0) {
            return notifications;
        }
        DBScheduler.customSubIds.addAll(subIds);
        subIds.forEach(key -> {
            CustomSubscription sub = customInMemoryCache.get(key);
            notifications.add(NotificationDto.mapToNotification(sub.userId(), sub.price(), sub.type(), SubType.CUSTOM,stock));
            customInMemoryCache.delete(key);
        });
        return notifications;
    }

    public List<NotificationDto> generateThreshold(Stock stock) {
        List<NotificationDto> notifications = new ArrayList<>();
        Map<Integer, ThresholdSubscription> map = thresholdInMemoryCache.getCache();
        List<Integer> bSubIds = map.entrySet().parallelStream().filter(entry -> entry.getValue().dailyLimit() < 1 && entry.getValue().stockId() == stock.id() && (entry.getValue().type().equals(PriceType.BUY) && compare(stock.buy(), entry.getValue().price() * (1 + entry.getValue().threshold() / 100.0)) >= 0 ||
                entry.getValue().type().equals(PriceType.BUY) && compare(stock.buy(), entry.getValue().price() * (1 - entry.getValue().threshold() / 100.0)) <= 0)).map(entry -> entry.getKey()).toList();
        List<Integer> sSubIds = map.entrySet().parallelStream().filter(entry -> entry.getValue().dailyLimit() < 1 && entry.getValue().stockId() == stock.id() && (entry.getValue().type().equals(PriceType.SELL) && compare(stock.sell(), entry.getValue().price() * (1 + entry.getValue().threshold() / 100.0)) >= 0 ||
                entry.getValue().type().equals(PriceType.SELL) && compare(stock.sell(), entry.getValue().price() * (1 - entry.getValue().threshold() / 100.0)) <= 0)).map(entry -> entry.getKey()).toList();

        if (bSubIds.size() > 0) {
            bSubIds.forEach(id -> {
                DBScheduler.prices.add(new PriceUpdateEntity(id, stock.buy()));
                ThresholdSubscription sub = thresholdInMemoryCache.get(id);
                notifications.add(NotificationDto.mapToNotification(sub.userId(), sub.price(), sub.type(), SubType.THRESHOLD,stock));
                thresholdInMemoryCache.update(new ThresholdSubscription(sub.id(), sub.userId(), sub.stockId(), stock.buy(), sub.threshold(), sub.type(), sub.dailyLimit() + 1));
            });
        }
        if (sSubIds.size() > 0) {
            sSubIds.forEach(id -> {
                DBScheduler.prices.add(new PriceUpdateEntity(id, stock.sell()));
                ThresholdSubscription sub = thresholdInMemoryCache.get(id);
                notifications.add(NotificationDto.mapToNotification(sub.userId(), sub.price(), sub.type(), SubType.THRESHOLD,stock));
                thresholdInMemoryCache.update(new ThresholdSubscription(sub.id(), sub.userId(), sub.stockId(), stock.sell(), sub.threshold(), sub.type(), sub.dailyLimit() + 1));

            });
        }

        return notifications;
    }


    private int compare(double x, double y) {
        if (Math.abs(x - y) < EPSILON) {
            return 0;
        } else if (x < y) {
            return -1;
        } else {
            return 1;
        }
    }

}
