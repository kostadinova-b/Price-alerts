package com.example.pricealertssubscrption.kafka;

import com.example.pricealertssubscrption.core.StockService;
import com.example.pricealertssubscrption.repository.StockRepository;
import com.example.pricealertssubscrption.repository.entity.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PriceListener {
    @Autowired
    private StockService stockService;
    @KafkaListener(topics = KafkaConfig.TOPIC_PRICES, groupId = KafkaConfig.CONSUMER_GROUP_ID)
    void listen(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Stock stock = mapper.readValue(data, Stock.class);
        stockService.updateStock(stock);

    }
}
