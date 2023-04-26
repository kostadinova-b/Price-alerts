package com.example.pricefeeder;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class PriceFeederApplication {

    @Autowired

    private PriceFeeder priceFeeder;

    public static void main(String[] args) throws JsonProcessingException {

        ApplicationContext context = SpringApplication.run(PriceFeederApplication.class, args);
        PriceFeederApplication app = context.getBean(PriceFeederApplication.class);
        app.start();
    }

    public void start() throws JsonProcessingException {
        priceFeeder.start();
    }


}
