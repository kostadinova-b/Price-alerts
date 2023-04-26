package com.example.notificationgenerator.repository.sql;

import com.example.notificationgenerator.entity.ThresholdSubscription;
import com.example.notificationgenerator.repository.ThresholdSubRepository;
import com.example.notificationgenerator.repository.entity.PriceUpdateEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MySQLThresholdRepository implements ThresholdSubRepository {

    private final TransactionTemplate tcTemplate;
    private final JdbcTemplate jdbc;

    public MySQLThresholdRepository(TransactionTemplate tcTemplate, JdbcTemplate jdbc) {
        this.tcTemplate = tcTemplate;
        this.jdbc = jdbc;
    }
    @Override
    public void batchPriceUpdate(List<PriceUpdateEntity> subPrices) {
        tcTemplate.execute(status -> {
            jdbc.batchUpdate(Queries.UPDATE_SUB, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setDouble(1, subPrices.get(i).price());
                    ps.setInt(2, subPrices.get(i).subId());
                }

                @Override
                public int getBatchSize() {
                    return subPrices.size();
                }
            });
            return null;
        });
    }


    static class Queries {
        public static final String UPDATE_SUB = "UPDATE threshold_subscriptions SET price = ? WHERE id = ?";
    }
}
