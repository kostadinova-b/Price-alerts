package com.example.notificationgenerator.repository;

import com.example.notificationgenerator.entity.ThresholdSubscription;
import com.example.notificationgenerator.repository.entity.PriceUpdateEntity;

import java.util.List;

public interface ThresholdSubRepository {
    void batchPriceUpdate(List<PriceUpdateEntity> subPrices);
}
