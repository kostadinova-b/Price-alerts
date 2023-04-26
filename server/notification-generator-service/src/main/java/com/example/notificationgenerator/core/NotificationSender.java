package com.example.notificationgenerator.core;

import com.example.notificationgenerator.dto.NotificationDto;
import com.example.notificationgenerator.entity.Stock;
import com.example.notificationgenerator.kafka.KafkaConfig;
import com.example.notificationgenerator.scheduled.HazelcastClusterChecker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class NotificationSender {

    @Autowired
    private ObjectMapper json;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private HazelcastNotificationGenerator hzGenerator;
    @Autowired
    private InMemoryNotificationGenerator mGenerator;

    @KafkaListener(topics = KafkaConfig.TOPIC_PRICES, groupId = "CG-${server.port}")
    public void onStockEvent(String data) throws JsonProcessingException {
        Stock stock = json.readValue(data, Stock.class);
        List<NotificationDto> notifications = new ArrayList<>();

        notifications.addAll(hzGenerator.getCustomNotifications(stock));
        notifications.addAll(hzGenerator.getThresholdNotifications(stock));

        notifications.forEach(dto -> {
            try {
                System.out.println("send notification: "+ dto.userId() +" "+ dto.subType() +" "+ dto.stockId());
                kafkaTemplate.send(KafkaConfig.TOPIC_NOTIFICATION, json.writeValueAsString(dto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
