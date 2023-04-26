package com.example.pricealertssubscrption.repository.sql;

import com.example.pricealertssubscrption.dto.PriceType;
import com.example.pricealertssubscrption.repository.CustomSubRepository;
import com.example.pricealertssubscrption.repository.entity.CustomSubscription;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@Component
public class MySQLCustomSubRepository implements CustomSubRepository {

    private final TransactionTemplate tcTemplate;
    private final JdbcTemplate jdbc;

    public MySQLCustomSubRepository(TransactionTemplate tcTemplate, JdbcTemplate jdbc) {
        this.tcTemplate = tcTemplate;
        this.jdbc = jdbc;
    }

    @Override
    public CustomSubscription createSubscription(int userId, int stockId, PriceType type, double price) {
        return tcTemplate.execute(stat -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();


            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(Queries.CREATE_SUBSCRIPTION, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setInt(2, stockId);
                ps.setString(3, type.toString());
                ps.setDouble(4, price);
                return ps;
            }, keyHolder);

            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return getSubscription(id);

        });
    }

    public CustomSubscription getSubscription(int id) {
        return jdbc.queryForObject(Queries.GET_SUB, (resultSet, rowNum) -> customSubMapper(resultSet), id);
    }

    @Override
    public void deleteSubscription(int id) {
        tcTemplate.execute(status -> {
            jdbc.update(Queries.DELETE_SUBSCRIPTION, id);
            return null;
        });
    }

    private CustomSubscription customSubMapper(ResultSet rs) throws SQLException {
        return new CustomSubscription(rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("stock_id"),
                rs.getDouble("on_price"),
                PriceType.valueOf(rs.getString("type")));
    }

    static class Queries {
        public static final String CREATE_SUBSCRIPTION = "INSERT INTO custom_subscriptions(user_id, stock_id, type, on_price) VALUES (?,?,?,?)";
        public static final String DELETE_SUBSCRIPTION = "DELETE FROM custom_subscriptions WHERE id = ?";
        public static final String GET_SUB = "SELECT id, user_id, stock_id, type, on_price FROM custom_subscriptions WHERE id = ?";
    }
}
