package com.example.pricefeeder.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic customTopic(){
        return TopicBuilder.name(KafkaConfig.TOPIC_PRICES).config(TopicConfig.RETENTION_MS_CONFIG, "86400000").build();
    }

}
