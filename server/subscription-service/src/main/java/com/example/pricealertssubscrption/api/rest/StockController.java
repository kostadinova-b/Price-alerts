package com.example.pricealertssubscrption.api.rest;

import com.example.pricealertssubscrption.core.StockService;
import com.example.pricealertssubscrption.repository.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class StockController {
    @Autowired
    private StockService stockService;

    @GetMapping("/stocks/{id}")
    public Stock getStock(@PathVariable int id){
        return stockService.getStock(id);
    }

    @GetMapping("/stocks")
    public List<Stock> getStocks(@RequestParam int page, @RequestParam int size){
        return stockService.getStocks(page, size);
    }

    @PostMapping("stocks")
    public void update(@RequestBody Stock stock){
        stockService.updateStock(stock);
    }
}
