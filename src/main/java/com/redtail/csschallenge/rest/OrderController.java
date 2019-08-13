package com.redtail.csschallenge.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @RequestMapping("/gretting")
    public String greeting() {
        return "Hello World!";
    }
}
