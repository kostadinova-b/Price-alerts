package com.example.pricealertssubscrption.repository.entity;

import com.example.pricealertssubscrption.dto.PriceType;

public class ThresholdSubscription {

    public final int id;
    public final int userId;
    public final int stockId;
    public final double price;
    public final int threshold;
    public final PriceType type;

    public ThresholdSubscription(int id, int userId, int stockId, double price, int threshold, PriceType type) {
        this.id = id;
        this.userId = userId;
        this.stockId = stockId;
        this.price = price;
        this.threshold = threshold;
        this.type = type;
    }
}
