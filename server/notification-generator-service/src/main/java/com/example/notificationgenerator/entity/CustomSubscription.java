package com.example.notificationgenerator.entity;

import java.io.Serializable;

public record CustomSubscription(int id, int userId, int stockId, double price, PriceType type) implements Serializable {

}
