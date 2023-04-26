package com.example.pricealertssubscrption.repository.entity;

import com.example.pricealertssubscrption.dto.PriceType;

public class CustomSubscription {

    public final int id;
    public final int userId;
    public final int stockId;
    public final double price;
    public final PriceType type;

    public CustomSubscription(int id, int userId, int stockId, double price, PriceType type) {
        this.id = id;
        this.userId = userId;
        this.stockId = stockId;
        this.price = price;
        this.type = type;
    }
}
