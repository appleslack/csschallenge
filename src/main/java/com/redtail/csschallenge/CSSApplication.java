package com.redtail.csschallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CSSApplication {

	public static void main(String[] args) {
		SpringApplication.run(CSSApplication.class, args);
		Restaurant myRestaurant = Restaurant.sharedInstance();
		myRestaurant.setName("The Bistro!");
		myRestaurant.startTakingOrders();
		
	}

}
