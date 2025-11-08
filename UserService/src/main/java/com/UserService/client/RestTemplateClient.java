package com.UserService.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RestTemplateClient {

    private final RestTemplate restTemplate;

    private static final String PROVIDER_URL= "http://localhost:8085";

    public String getInstanceInfo() {
        return restTemplate.getForObject(PROVIDER_URL + "/provider", String.class);
    }
}
