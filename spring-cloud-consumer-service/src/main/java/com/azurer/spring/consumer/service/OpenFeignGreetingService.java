package com.azurer.spring.consumer.service;

import com.azurer.spring.consumer.entity.Greeting;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "PROVIDER-SERVICE")
public interface OpenFeignGreetingService {
    @GetMapping(value = "/greeting")
    Greeting getRemoteGreeting(@RequestParam(value = "name", defaultValue = "World") String name);

    @GetMapping(value = "/greeting/waiting")
    Greeting getRemoteGreetingWaiting(@RequestParam(value = "name", defaultValue = "World") String name);
}
