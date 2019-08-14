package com.redtail.csschallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class CSSApplication {

	public static void main(String[] args) {
		SpringApplication.run(CSSApplication.class, args);
		Restaurant myRestaurant = Restaurant.sharedInstance();
		myRestaurant.setName("Cody's Fine Foods");
		myRestaurant.startTakingOrders();
		
	}

}
