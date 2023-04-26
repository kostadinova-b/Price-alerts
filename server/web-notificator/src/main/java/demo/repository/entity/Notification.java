package demo.repository.entity;

public record Notification(int id, int userId, Status status, int retryCount) {
}
