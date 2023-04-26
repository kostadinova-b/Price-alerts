package com.example.dbfeeder.core_mock;

import com.example.dbfeeder.entity.Stock;
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
public class ThresholdSubFeeder {
    private final TransactionTemplate tcTemplate;
    private final JdbcTemplate jdbc;

    public ThresholdSubFeeder(TransactionTemplate tcTemplate, JdbcTemplate jdbc) {
        this.tcTemplate = tcTemplate;
        this.jdbc = jdbc;
    }

    private List<Stock> getStocks(){
        return jdbc.query("SELECT id, buy, sell FROM stocks", (rs, rNum) -> {
            return new Stock(rs.getInt("id"), rs.getDouble("buy"), rs.getDouble("sell"));
        });
    }

    public void generateThreshold(int usersCount, int stocksCount){
        List<Integer> range = IntStream.rangeClosed(1, 2*usersCount*stocksCount)
                .boxed().toList();

        List<Stock> stocks = getStocks();
        Random rand = new Random();

        tcTemplate.execute(status -> {
            jdbc.batchUpdate("INSERT INTO threshold_subscriptions(id, user_id, stock_id, type, price, threshold, rates_per_day) VALUES (?, ?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    int sub = range.get(i);
                    int stock = sub%2 == 0 ? (sub / 2)% stocksCount : ((sub + 1)/2)%stocksCount;
                    if(stock == 0){
                        stock = stocksCount;
                    }
                    double price = sub%2 == 0 ? stocks.get(stock - 1).buy() : stocks.get(stock - 1).sell();
                    ps.setInt(1, sub);
                    ps.setInt(2, (int)Math.ceil(sub / (stocksCount*2*1.0)));
                    ps.setInt(3, stock);
                    ps.setString(4, sub%2 == 0 ? "BUY" : "SELL");
                    ps.setDouble(5, price);
                    ps.setInt(6, rand.nextInt(100) + 1);
                    ps.setInt(7, 1);
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
