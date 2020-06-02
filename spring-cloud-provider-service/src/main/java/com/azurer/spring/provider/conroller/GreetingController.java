package com.azurer.spring.provider.conroller;


import com.azurer.spring.provider.entity.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@Slf4j
public class GreetingController {

    @Value("${server.port}")
    private Integer port;

    private static final String template = "From Provider %d: Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, port, name));
    }

    @GetMapping("/greeting/waiting")
    public Greeting greetingWaiting(@RequestParam(value = "name", defaultValue = "World") String name) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
        return new Greeting(counter.incrementAndGet(), String.format(template, port, name));
    }
}
