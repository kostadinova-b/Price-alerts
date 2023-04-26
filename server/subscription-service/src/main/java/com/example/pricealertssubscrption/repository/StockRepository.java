package com.example.pricealertssubscrption.repository;

import com.example.pricealertssubscrption.repository.entity.Stock;

import java.util.List;

public interface StockRepository {

    Stock createStock(double buy, double sell);

    Stock getStock(int id);

    void updateStock(Stock stock);

    List<Stock> getStocks(int page, int size);
}
