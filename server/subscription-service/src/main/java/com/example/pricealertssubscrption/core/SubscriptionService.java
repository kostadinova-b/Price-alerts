package com.example.pricealertssubscrption.core;

import com.example.pricealertssubscrption.dto.PriceType;
import com.example.pricealertssubscrption.kafka.KafkaConfig;
import com.example.pricealertssubscrption.repository.CustomSubRepository;
import com.example.pricealertssubscrption.repository.ThresholdSubRepository;
import com.example.pricealertssubscrption.repository.entity.CustomSubscription;
import com.example.pricealertssubscrption.repository.entity.ThresholdSubscription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private CustomSubRepository customSubRepository;
    @Autowired
    private ThresholdSubRepository thresholdSubRepository;
    @Autowired
    private ObjectMapper json;

    public void subscribeCustom(int userId, int stockId, PriceType type, double price) throws JsonProcessingException {
        CustomSubscription sub = customSubRepository.createSubscription(userId, stockId, type, price);
        System.out.println("send sub: "+sub.toString());
        kafkaTemplate.send(KafkaConfig.TOPIC_CUSTOM_SUBSCRIPTIONS, json.writeValueAsString(sub));

    }

    public void subscribeThreshold(int userId, int stockId, PriceType type, double price, int threshold) throws JsonProcessingException {
        ThresholdSubscription sub = thresholdSubRepository.createSubscription(userId, stockId, type, price, threshold);
        System.out.println("send sub: "+sub.toString());
        kafkaTemplate.send(KafkaConfig.TOPIC_THRESHOLD_SUBSCRIPTIONS, json.writeValueAsString(sub));
    }
}
