package com.example.pricealertssubscrption.core;

import com.example.pricealertssubscrption.repository.StockRepository;
import com.example.pricealertssubscrption.repository.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.CacheEvictOperation;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Cacheable(cacheNames = "stocks", key = "#id")
    public Stock getStock(int id){
        return stockRepository.getStock(id);
    }

    public List<Stock> getStocks(int page, int size){
        return stockRepository.getStocks(page, size);
    }

    @CachePut(cacheNames = "stocks", key = "#stock.id")
    @Cacheable(cacheNames = "stocks", key = "#stock.id")
    public Stock updateStock(Stock stock){
//        System.out.println("cache added");
//        System.out.println(stock.toString());
        return stock;
    }
}
