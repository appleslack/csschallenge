package com.redtail.csschallenge.rest;

import com.redtail.csschallenge.Restaurant;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @RequestMapping("/orderRandomItem")
    public String orderRandomItem() {
        String itemName;
        try {
            itemName = Restaurant.sharedInstance().orderRandomItem();
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception caught while ordering";
        }

        return "Ordered " + itemName;
    }
}
