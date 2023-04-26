package com.example.pricealertssubscrption.api.rest;

import com.example.pricealertssubscrption.core.SubscriptionService;
import com.example.pricealertssubscrption.dto.SubType;
import com.example.pricealertssubscrption.dto.SubscriptionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;
    @PostMapping("/subscriptions")
    public void subscribe(@RequestBody SubscriptionDto subDto) throws JsonProcessingException {
        if(subDto.sType().equals(SubType.THRESHOLD)){
            subscriptionService.subscribeThreshold(subDto.userId(), subDto.stockId(), subDto.pType(), subDto.price(), subDto.threshold());
        } else {
            subscriptionService.subscribeCustom(subDto.userId(), subDto.stockId(), subDto.pType(), subDto.price());
        }
    }
}
