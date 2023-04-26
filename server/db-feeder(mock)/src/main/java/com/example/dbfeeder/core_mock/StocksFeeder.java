package com.example.dbfeeder.core_mock;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class StocksFeeder {
    private final TransactionTemplate tcTemplate;
    private final JdbcTemplate jdbc;

    public StocksFeeder(TransactionTemplate tcTemplate, JdbcTemplate jdbc) {
        this.tcTemplate = tcTemplate;
        this.jdbc = jdbc;
    }

    public void generateStocks(int count){
        List<Integer> range = IntStream.rangeClosed(1, count)
                .boxed().toList();
        Random rand = new Random();

        tcTemplate.execute(status -> {
            jdbc.batchUpdate("INSERT INTO stocks(id, buy, sell) VALUES (?, ?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    double buy = rand.nextDouble()*1000;
                    ps.setInt(1, range.get(i));
                    ps.setDouble(2, buy);
                    ps.setDouble(3, buy - rand.nextDouble()*10 );
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
