package com.example.notificationgenerator.dto;

import com.example.notificationgenerator.entity.PriceType;
import com.example.notificationgenerator.entity.Stock;

public record NotificationDto(int userId, int stockId, SubType subType, PriceType pType, double price, Direction direction) {
    public static NotificationDto mapToNotification(int userId, double notifiedPrice, PriceType type, SubType subType, Stock stock){
        double currPrice = type.equals(PriceType.BUY) ? stock.buy() : stock.sell();
        Direction dir = currPrice > notifiedPrice ? Direction.UP : Direction.DOWN;
        return new NotificationDto(userId, stock.id(), subType, type, currPrice, dir);
    }
}
