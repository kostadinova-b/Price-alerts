package com.example.notificationgenerator.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic notifTopic(){
        return TopicBuilder.name(KafkaConfig.TOPIC_NOTIFICATION).config(TopicConfig.RETENTION_MS_CONFIG, "86400000").build();
    }
}
