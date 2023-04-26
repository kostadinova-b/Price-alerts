package com.example.notificationgenerator.core;

import com.example.notificationgenerator.dto.NotificationDto;
import com.example.notificationgenerator.dto.SubType;
import com.example.notificationgenerator.entity.CustomSubscription;
import com.example.notificationgenerator.entity.PriceType;
import com.example.notificationgenerator.entity.Stock;
import com.example.notificationgenerator.entity.ThresholdSubscription;
import com.example.notificationgenerator.repository.HazelcastCache;
import com.example.notificationgenerator.repository.entity.PriceUpdateEntity;
import com.example.notificationgenerator.scheduled.DBScheduler;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HazelcastNotificationGenerator {

    private final double EPSILON = 10e-5;
    @Autowired
    private IMap<Integer, CustomSubscription> sImap;
    @Autowired
    private HazelcastCache cache;
    @Autowired
    private IMap<Integer, ThresholdSubscription> tImap;
//    @Autowired
//    private DBScheduler dbScheduler;

    public static Set<Integer> customNotified = Collections.newSetFromMap(new ConcurrentHashMap<>());
    public static Set<Integer> thresholdNotified = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public List<NotificationDto> getCustomNotifications(Stock stock){
        Predicate<Integer, CustomSubscription> predicate = entry -> {
            return !customNotified.contains(entry.getKey()) && entry.getValue().stockId() == stock.id() && (entry.getValue().type().equals(PriceType.BUY) && compare(entry.getValue().price(), stock.buy()) == 0 ||
                    entry.getValue().type().equals(PriceType.SELL) && compare(entry.getValue().price(), stock.sell()) == 0);
        };

        Set<Integer> keys = sImap.localKeySet(predicate);
        List<NotificationDto> notifications = new ArrayList<>();
        if(keys.size() > 0){

            customNotified.addAll(keys);
            DBScheduler.customSubIds.addAll(keys);

            List<CustomSubscription> subs = cache.getCustomSubscriptionsByIds(keys);
            subs.forEach(sub -> {
                NotificationDto dto = NotificationDto.mapToNotification(sub.userId(), sub.price(), sub.type(), SubType.CUSTOM,stock);
                notifications.add(dto);
            });
        }

        return notifications;
    }

    public List<NotificationDto> getThresholdNotifications(Stock stock){

        Predicate<Integer, ThresholdSubscription> predicateBuy = entry -> {
            return !thresholdNotified.contains(entry.getKey()) && entry.getValue().stockId() == stock.id() &&
                    ((entry.getValue().type().equals(PriceType.BUY) && (compare(stock.buy(), entry.getValue().price() * (1 + entry.getValue().threshold() / 100.0)) >= 0)) ||
                            (entry.getValue().type().equals(PriceType.BUY) && (compare(stock.buy(), entry.getValue().price() * (1 - entry.getValue().threshold() / 100.0)) <= 0)));
        };
        Predicate<Integer, ThresholdSubscription> predicateSell = entry -> {
            return !thresholdNotified.contains(entry.getKey()) && entry.getValue().stockId() == stock.id() &&
                    ((entry.getValue().type().equals(PriceType.SELL) && (compare(stock.sell(), entry.getValue().price() * (1 + entry.getValue().threshold() / 100.0)) >= 0)) ||
                    (entry.getValue().type().equals(PriceType.SELL) && (compare(stock.sell(), entry.getValue().price() * (1 - entry.getValue().threshold() / 100.0)) <= 0)));

        };

        Set<Integer> bkeys = tImap.localKeySet(predicateBuy);
        Set<Integer> skeys = tImap.localKeySet(predicateSell);
        List<NotificationDto> notifications = new ArrayList<>();

        if(bkeys.size() > 0){
            thresholdNotified.addAll(bkeys);
            cache.getThresholdSubscriptionsByIds(bkeys).forEach(sub -> {
                DBScheduler.prices.add(new PriceUpdateEntity(sub.id(),stock.buy()));
                NotificationDto dto = NotificationDto.mapToNotification(sub.userId(), sub.price(), sub.type(), SubType.THRESHOLD, stock);
                notifications.add(dto);
            });
        }

        if(skeys.size() > 0){
            thresholdNotified.addAll(skeys);
            cache.getThresholdSubscriptionsByIds(skeys).forEach(sub -> {
                DBScheduler.prices.add(new PriceUpdateEntity(sub.id(),stock.sell()));
                NotificationDto dto = NotificationDto.mapToNotification(sub.userId(), sub.price(), sub.type(), SubType.THRESHOLD,stock);
                notifications.add(dto);
            });
        }


        return notifications;
    }

    private int compare(double x, double y){
        if(Math.abs(x - y) < EPSILON){
            return 0;
        } else if (x < y){
            return -1;
        } else {
            return 1;
        }
    }

}
