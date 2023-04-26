package com.example.dbfeeder;

import com.example.dbfeeder.core_mock.CustomSubFeeder;
import com.example.dbfeeder.core_mock.StocksFeeder;
import com.example.dbfeeder.core_mock.ThresholdSubFeeder;
import com.example.dbfeeder.core_mock.UserFeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
public class DbFeederApplication {

	@Autowired
	private UserFeeder userFeeder;
	@Autowired
	private StocksFeeder stocksFeeder;
	@Autowired
	private CustomSubFeeder customSubFeeder;
	@Autowired
	private ThresholdSubFeeder thresholdSubFeeder;

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DbFeederApplication.class, args);
		DbFeederApplication app = context.getBean(DbFeederApplication.class);
		app.start();
	}

	private void start() {
		userFeeder.generateUsers(10);
		stocksFeeder.generateStocks(40000);
//		customSubFeeder.generateCustom( 10, 40000);
//		thresholdSubFeeder.generateThreshold(10, 40000);
	}

}
