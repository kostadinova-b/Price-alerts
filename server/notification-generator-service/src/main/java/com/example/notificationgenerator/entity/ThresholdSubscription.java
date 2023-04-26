package com.example.notificationgenerator.entity;

import java.io.Serializable;

public record ThresholdSubscription(int id, int userId, int stockId, double price, int threshold, PriceType type, int dailyLimit) implements Serializable {

}