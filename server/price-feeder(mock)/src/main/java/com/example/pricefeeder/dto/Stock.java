package com.example.pricefeeder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Stock implements Serializable {
    public final int id;
    public final double buy;
    public final double sell;

    @JsonCreator
    public Stock(@JsonProperty("id") int id, @JsonProperty("buy") double buy, @JsonProperty("sell") double sell) {
        this.id = id;
        this.buy = buy;
        this.sell = sell;
    }

}
