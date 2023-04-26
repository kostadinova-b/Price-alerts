package com.example.pricealertssubscrption.kafka;

public class KafkaConfig {
    public static final String TOPIC_PRICES = "prices";
    public static final String TOPIC_CUSTOM_SUBSCRIPTIONS = "custom_subs";
    public static final String TOPIC_THRESHOLD_SUBSCRIPTIONS = "threshold_subs";
    public static final String CONSUMER_GROUP_ID = "subscription-service";
}
