package com.UserService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="provider", url="http://localhost:8085")
public interface ProviderFeignClient {

    @GetMapping("/provider")
    String getInstanceInfo();
}
