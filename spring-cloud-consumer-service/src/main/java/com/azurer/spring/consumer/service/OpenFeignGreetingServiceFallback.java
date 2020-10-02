package com.azurer.spring.consumer.service;

import com.azurer.spring.consumer.entity.Greeting;
import org.springframework.stereotype.Service;

@Service
public class OpenFeignGreetingServiceFallback implements OpenFeignGreetingService {
    @Override
    public Greeting getRemoteGreeting(String name) {
        return new Greeting(-1, "RemoteGreeting Fallback:"+name);
    }

    @Override
    public Greeting getRemoteGreetingWaiting(String name) {
        return new Greeting(-2, "RemoteGreetingWaiting Fallback:"+name);
    }
}
