package demo.repository.mysql;

import demo.repository.NotificationRepository;
import demo.repository.entity.Notification;
import demo.repository.entity.Status;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

@Repository
public class MySqlNotificationRepository implements NotificationRepository {

    private JdbcTemplate jdbc;
    private TransactionTemplate tcTemplate;

    public MySqlNotificationRepository(JdbcTemplate jdbc, TransactionTemplate tcTemplate) {
        this.jdbc = jdbc;
        this.tcTemplate = tcTemplate;
    }

    @Override
    public int addNotification(int userId, int subId) {
        return tcTemplate.execute(stat -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(Queries.CREATE_NOTIF, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setInt(2, subId);
                return ps;
            }, keyHolder);

            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return id;
        });

    }

    @Override
    public void incrementRetryCount(long id) {

    }

    @Override
    public void deleteNotification(long id) {

    }

    @Override
    public void changeStatus(long id, Status newStatus) {

    }

    @Override
    public Status getStatus(long id) {
        return null;
    }


    static class Queries{
        public static final String CREATE_NOTIF = "INSERT INTO notifications(user_id, sub_id) VALUES (?,?)";
        public static final String DELETE_NOTIF = "";
        public static final String INCREMENT_RETRY_COUNT = "";
        public static final String GET_STATUS = "";
        public static final String CHANGE_STATUS = "";
    }
}
