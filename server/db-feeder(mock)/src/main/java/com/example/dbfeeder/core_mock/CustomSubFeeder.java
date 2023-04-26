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
public class CustomSubFeeder {
    private final TransactionTemplate tcTemplate;
    private final JdbcTemplate jdbc;

    public CustomSubFeeder(TransactionTemplate tcTemplate, JdbcTemplate jdbc) {
        this.tcTemplate = tcTemplate;
        this.jdbc = jdbc;
    }

    public void generateCustom(int usersCount, int stocksCount){
        List<Integer> range = IntStream.rangeClosed(1, 2*usersCount*stocksCount)
                .boxed().toList();

        Random rand = new Random();

        tcTemplate.execute(status -> {
            jdbc.batchUpdate("INSERT INTO custom_subscriptions(id, user_id, stock_id, type, on_price) VALUES (?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    int sub = range.get(i);
                    int stock = sub%2 == 0 ? (sub / 2)% stocksCount : ((sub + 1)/2)%stocksCount;
                    if(stock == 0){
                        stock = stocksCount;
                    }
                    ps.setInt(1, sub);
                    ps.setInt(2, (int)Math.ceil(sub / (stocksCount*2*1.0)));
                    ps.setInt(3, stock);
                    ps.setString(4, sub%2 == 0 ? "BUY" : "SELL");
                    ps.setDouble(5, rand.nextDouble()*1000);
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
