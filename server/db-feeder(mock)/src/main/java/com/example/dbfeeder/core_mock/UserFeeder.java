package com.example.dbfeeder.core_mock;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class UserFeeder {

    private final TransactionTemplate tcTemplate;
    private final JdbcTemplate jdbc;

    public UserFeeder(TransactionTemplate tcTemplate, JdbcTemplate jdbc) {
        this.tcTemplate = tcTemplate;
        this.jdbc = jdbc;
    }

    public void generateUsers(int count){
        List<Integer> range = IntStream.rangeClosed(1, count)
                .boxed().toList();

        tcTemplate.execute(status -> {
            jdbc.batchUpdate("INSERT INTO users(id) VALUES (?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, range.get(i));
                }

                @Override
                public int getBatchSize() {
                    return range.size();
                }
            });
            return null;
        });
    }
}
