package com.example.pricealertssubscrption.repository.sql;

import com.example.pricealertssubscrption.dto.PriceType;
import com.example.pricealertssubscrption.repository.ThresholdSubRepository;
import com.example.pricealertssubscrption.repository.entity.ThresholdSubscription;
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
public class MySQLThresholdSubRepository implements ThresholdSubRepository {

    private final TransactionTemplate tcTemplate;
    private final JdbcTemplate jdbc;

    public MySQLThresholdSubRepository(TransactionTemplate tcTemplate, JdbcTemplate jdbc) {
        this.tcTemplate = tcTemplate;
        this.jdbc = jdbc;
    }

    @Override
    public ThresholdSubscription createSubscription(int userId, int stockId, PriceType type, double price, int threshold) {
        return tcTemplate.execute(stat -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();


            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(Queries.CREATE_SUBSCRIPTION, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setInt(2, stockId);
                ps.setString(3, type.toString());
                ps.setDouble(4, price);
                ps.setInt(5, threshold);
                ps.setInt(6, 0);
                return ps;
            }, keyHolder);

            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return getSubscription(id);

        });


    }

    public ThresholdSubscription getSubscription(int id) {
        return jdbc.queryForObject(Queries.GET_SUB, (resultSet, rowNum) -> thresholdSubMapper(resultSet), id);
    }

    private ThresholdSubscription thresholdSubMapper(ResultSet rs) throws SQLException {
        return new ThresholdSubscription(rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("stock_id"),
                rs.getDouble("price"),
                rs.getInt("threshold"),
                PriceType.valueOf(rs.getString("type")));
    }

    static class Queries {
        public static final String CREATE_SUBSCRIPTION = "INSERT INTO threshold_subscriptions(user_id, stock_id, type, price, threshold, rates_per_day) VALUES (?,?,?,?,?, ?)";
        public static final String GET_SUB = "SELECT id, user_id, stock_id, type, price, threshold FROM threshold_subscriptions WHERE id = ?";
    }
}
