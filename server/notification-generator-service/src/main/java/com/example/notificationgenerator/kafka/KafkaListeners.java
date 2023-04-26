package com.example.notificationgenerator.kafka;

import com.example.notificationgenerator.entity.CustomSubscription;
import com.example.notificationgenerator.entity.ThresholdSubscription;
import com.example.notificationgenerator.repository.CustomInMemoryCache;
import com.example.notificationgenerator.repository.HazelcastCache;
import com.example.notificationgenerator.repository.ThresholdInMemoryCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    @Autowired
    private ObjectMapper json;
    @Autowired
    private HazelcastCache cache;
    @Autowired
    private ThresholdInMemoryCache thresholdInMemoryCache;
    @Autowired
    private CustomInMemoryCache customInMemoryCache;

    @KafkaListener(topics = KafkaConfig.TOPIC_CUSTOM_SUBSCRIPTIONS, groupId = KafkaConfig.SUBSCRIPTION_GROUP_ID)
    public void onCustomSubEvents(String data) throws JsonProcessingException {
        System.out.println("received custom");
        CustomSubscription sub = json.readValue(data, CustomSubscription.class);
        cache.addCustom(sub.id(), sub);
        customInMemoryCache.insert(sub);
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_THRESHOLD_SUBSCRIPTIONS, groupId = KafkaConfig.SUBSCRIPTION_GROUP_ID)
    public void onThresholdSubEvents(String data) throws JsonProcessingException {
        System.out.println("received thresh");
        ThresholdSubscription sub = json.readValue(data, ThresholdSubscription.class);
        cache.addThreshold(sub.id(), sub);
        thresholdInMemoryCache.insert(sub);
    }
}