package com.UserService.feign;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign")
@RequiredArgsConstructor
public class FeignController {

    private final ProviderFeignClient providerFeignClient;

    @GetMapping
    public String getInstanceInfo() {
        return providerFeignClient.getInstanceInfo();
    }
}
