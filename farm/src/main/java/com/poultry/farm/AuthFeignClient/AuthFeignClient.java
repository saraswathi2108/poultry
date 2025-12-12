package com.poultry.farm.AuthFeignClient;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "AuthService", url = "http://localhost:8083")
public interface AuthFeignClient {

    @PostMapping("/api/auth/validate")
    Map<String, Object> validateToken(@RequestBody Map<String, String> request);
}


