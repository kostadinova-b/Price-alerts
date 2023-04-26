package com.example.pricealertssubscrption.repository;

import com.example.pricealertssubscrption.dto.PriceType;
import com.example.pricealertssubscrption.repository.entity.CustomSubscription;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomSubRepository {

    CustomSubscription createSubscription(int userId, int stockId, PriceType type, double price);

    CustomSubscription getSubscription(int id);

    void deleteSubscription(int id);
}
