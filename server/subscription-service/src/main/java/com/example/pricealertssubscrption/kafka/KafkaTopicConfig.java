package com.example.pricealertssubscrption.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.subs.partitions.count}")
    private int partitionCount;
    @Value("${kafka.topic.subs.partitions.replica.count}")
    private int replicaCount;

    @Bean
    public NewTopic customSubTopic() {
        return TopicBuilder.name(KafkaConfig.TOPIC_CUSTOM_SUBSCRIPTIONS)
                .partitions(partitionCount)
                .replicas(replicaCount).build();
    }

    @Bean
    public NewTopic thresholdSubTopic() {
        return TopicBuilder.name(KafkaConfig.TOPIC_THRESHOLD_SUBSCRIPTIONS).partitions(partitionCount)
                .replicas(replicaCount).build();
    }
}
