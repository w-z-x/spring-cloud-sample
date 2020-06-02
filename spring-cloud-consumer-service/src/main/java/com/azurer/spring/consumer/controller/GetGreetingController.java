package com.azurer.spring.consumer.controller;

import com.azurer.spring.consumer.entity.Greeting;
import com.azurer.spring.consumer.service.GreetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class GetGreetingController {
    @Resource
    private GreetingService greetingService;

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return greetingService.getRemoteGreeting(name);
    }
}
