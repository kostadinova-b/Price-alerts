package demo.repository;

import demo.repository.entity.Notification;
import demo.repository.entity.Status;

public interface NotificationRepository {
    int addNotification(int userId, int subId);
    void incrementRetryCount(long id);
    void deleteNotification(long id);
    void changeStatus(long id, Status newStatus);
    Status getStatus(long id);
}
