package com.example.notificationgenerator.repository.sql;

import com.example.notificationgenerator.repository.CustomSubRepository;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MySQLCustomSubRepository implements CustomSubRepository {

    private final TransactionTemplate tcTemplate;
    private final JdbcTemplate jdbc;

    public MySQLCustomSubRepository(TransactionTemplate tcTemplate, JdbcTemplate jdbc) {
        this.tcTemplate = tcTemplate;
        this.jdbc = jdbc;
    }

    @Override
    public void batchDelete(List<Integer> ids) {
        tcTemplate.execute(status -> {
            jdbc.batchUpdate(Queries.DELETE_SUB, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, ids.get(i));
                }

                @Override
                public int getBatchSize() {
                    return ids.size();
                }
            });
            return null;
        });
    }

    static class Queries {
        public static final String DELETE_SUB = "DELETE FROM custom_subscriptions WHERE id = ?";
    }
}
