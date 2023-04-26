package com.example.pricealertssubscrption.repository;

import com.example.pricealertssubscrption.dto.PriceType;
import com.example.pricealertssubscrption.repository.entity.ThresholdSubscription;
import org.springframework.stereotype.Repository;

@Repository
public interface ThresholdSubRepository {

    ThresholdSubscription createSubscription(int userId, int stockId, PriceType type, double price, int threshold);

    ThresholdSubscription getSubscription(int id);
}
