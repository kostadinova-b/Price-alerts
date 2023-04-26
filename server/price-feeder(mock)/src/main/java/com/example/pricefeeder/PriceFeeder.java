package com.example.pricefeeder;

import com.example.pricefeeder.config.KafkaConfig;
import com.example.pricefeeder.dto.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class PriceFeeder {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void start() throws JsonProcessingException {
        int id = 0;
        double buy = 10.2;
        double sell = 13.3;
        DecimalFormat df = new DecimalFormat("#.###");
        ObjectMapper mapper = new ObjectMapper();
        while (true) {
            id = 1;
            buy += 0.002;
            sell += 0.003;
            Stock s = new Stock(id, Double.parseDouble(df.format(buy)), Double.parseDouble(df.format(sell)));
            kafkaTemplate.send(KafkaConfig.TOPIC_PRICES, mapper.writeValueAsString(s));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
