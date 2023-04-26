package com.example.notificationgenerator.scheduled;

import com.hazelcast.cluster.Cluster;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

public class HazelcastClusterChecker {
    public static volatile boolean isClusterUp;
    public static volatile boolean isNodeUp;
    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Scheduled(fixedRateString = "${hazelcast.cluster.checker.delay}")
    public void checkClusterState(){
        Cluster cluster = hazelcastInstance.getCluster();
        isClusterUp = !cluster.getMembers().isEmpty();
    }

    @Scheduled(fixedRateString = "${hazelcast.node.checker.delay}")
    public void checkNodeState(){
        isNodeUp = hazelcastInstance != null && hazelcastInstance.getLifecycleService().isRunning();
    }


}
