package com.example.pricealertssubscrption.repository.sql;

import com.example.pricealertssubscrption.repository.StockRepository;
import com.example.pricealertssubscrption.repository.entity.Stock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Component
public class MySQLStockRepository implements StockRepository {

    private final TransactionTemplate tcTemplate;
    private final JdbcTemplate jdbc;

    public MySQLStockRepository(TransactionTemplate tcTemplate, JdbcTemplate jdbc) {
        this.tcTemplate = tcTemplate;
        this.jdbc = jdbc;
    }

    @Override
    public Stock createStock(double buy, double sell) {
        return tcTemplate.execute(stat -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(Queries.CREATE_STOCK, Statement.RETURN_GENERATED_KEYS);
                ps.setDouble(1, buy);
                ps.setDouble(2, sell);
                return ps;
            }, keyHolder);

            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return getStock(id);
        });

    }

    @Override
    public Stock getStock(int id) {
        System.out.println("in db");
        return jdbc.queryForObject(Queries.GET_STOCK, (resultSet, rowNum) -> stockMapper(resultSet), id);
    }

    @Override
    public void updateStock(Stock stock) {

        tcTemplate.execute(stat -> {
            jdbc.update(Queries.UPDATE_STOCK, stock.buy(), stock.sell(), stock.id());
            return null;
        });

    }

    @Override
    public List<Stock> getStocks(int page, int size) {
        return jdbc.query(Queries.GET_STOCKS, (resultSet, rowNum) -> stockMapper(resultSet), page * size, size);
    }

    private Stock stockMapper(ResultSet rs) throws SQLException {
        return new Stock(rs.getInt("id"),
                rs.getDouble("buy"),
                rs.getDouble("sell"));
    }

    static class Queries {
        public static final String CREATE_STOCK = "INSERT INTO stocks(buy, sell) VALUES (?,?)";
        public static final String GET_STOCK = "SELECT id, buy, sell FROM stocks WHERE id = ?";
        public static final String UPDATE_STOCK = "UPDATE stocks SET buy = ?, sell = ? WHERE id = ?";
        public static final String GET_STOCKS = "SELECT id, buy, sell FROM stocks LIMIT ?,?";
    }
}
