package com.azurer.spring.consumer.controller;

import com.azurer.spring.consumer.entity.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GetGreetingController {

    @Resource
    public RestTemplate restTemplate;

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {

        Map<String, String> params = new HashMap<>();
        params.put("name", name.toUpperCase());

        Greeting greeting = restTemplate.getForObject(
                "http://localhost:8080/provider-service/greeting?name={name}", Greeting.class, params);

        return greeting;
    }
}
