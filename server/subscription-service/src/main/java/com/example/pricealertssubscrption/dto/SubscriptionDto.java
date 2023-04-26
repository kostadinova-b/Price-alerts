package com.example.pricealertssubscrption.dto;

import java.util.Optional;

public record SubscriptionDto(int userId, int stockId, SubType sType, PriceType pType,double price, int threshold) {
}
