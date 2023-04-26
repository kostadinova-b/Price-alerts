package demo.dto;

public record NotificationDto(int userId, int stockId, SubType subType, PriceType pType, double price, Direction direction) {

}
