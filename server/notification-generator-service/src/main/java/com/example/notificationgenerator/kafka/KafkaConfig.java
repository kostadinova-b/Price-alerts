package com.example.notificationgenerator.kafka;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public class KafkaConfig {

    public static final String TOPIC_PRICES = "prices";
    public static final String TOPIC_CUSTOM_SUBSCRIPTIONS = "custom_subs";
    public static final String TOPIC_THRESHOLD_SUBSCRIPTIONS = "threshold_subs";
    public static final String TOPIC_NOTIFICATION = "notifications";
    public static final String SUBSCRIPTION_GROUP_ID = "sub_group";
}
