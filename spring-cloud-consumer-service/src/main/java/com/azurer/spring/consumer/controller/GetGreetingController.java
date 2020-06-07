package com.azurer.spring.consumer.controller;

import com.azurer.spring.consumer.entity.Greeting;
import com.azurer.spring.consumer.service.HystrixGreetingService;
import com.azurer.spring.consumer.service.OpenFeignGreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class GetGreetingController {
    @Resource
    private HystrixGreetingService hystrixGreetingService;

    @Resource
    private OpenFeignGreetingService openFeignGreetingService;

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        log.info("Greeting {}", name);
        return hystrixGreetingService.getRemoteGreeting(name);
    }

    @GetMapping("/greeting/openfeign")
    public Greeting greetingByOpenFeign(@RequestParam(value = "name", defaultValue = "World") String name) {
        return openFeignGreetingService.getRemoteGreeting(name + " [OpenFeign]");
    }

    @GetMapping("/greeting/openfeign/waiting")
    public Greeting greetingByOpenFeignWaiting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return openFeignGreetingService.getRemoteGreetingWaiting(name + " [OpenFeign][Waiting]");
    }
}
