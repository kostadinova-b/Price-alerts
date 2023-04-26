package com.example.notificationgenerator.hazelcast;

import com.example.notificationgenerator.entity.CustomSubscription;
import com.example.notificationgenerator.entity.ThresholdSubscription;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.partition.PartitionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.core.IExecutorService;

@Configuration
public class HazelcastCofig {

    private final String CUSTOM_SUB_TABLE = "custom";
    private final String THRESHOLD_SUB_TABLE = "threshold";

    @Bean(name = "hcastBean")
    public HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance();
    }

    @Bean
    public PartitionService partitionService(@Qualifier("hcastBean") HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getPartitionService();
    }


    @Bean
    public IMap<Integer, ThresholdSubscription> threshMap(@Qualifier("hcastBean") HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap(THRESHOLD_SUB_TABLE);
    }

    @Bean
    public IMap<Integer, CustomSubscription> customhMap(@Qualifier("hcastBean") HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap(CUSTOM_SUB_TABLE);
    }

    @Bean
    public IExecutorService service(@Qualifier("hcastBean") HazelcastInstance hazelcastInstance){
        return hazelcastInstance.getExecutorService("local");
    }
}
