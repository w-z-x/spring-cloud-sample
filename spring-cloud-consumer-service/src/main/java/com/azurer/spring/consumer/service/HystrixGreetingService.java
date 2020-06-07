package com.azurer.spring.consumer.service;

import com.azurer.spring.consumer.entity.Greeting;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class HystrixGreetingService {
    @Resource
    public RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "reliable")
    public Greeting getRemoteGreeting(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name.toUpperCase());
        return restTemplate.getForObject(
                "http://localhost:8082/greeting?name={name}", Greeting.class, params);
    }

    public Greeting reliable(String name) {
        return new Greeting(-1, "Bad Provider, " + name);
    }
}
