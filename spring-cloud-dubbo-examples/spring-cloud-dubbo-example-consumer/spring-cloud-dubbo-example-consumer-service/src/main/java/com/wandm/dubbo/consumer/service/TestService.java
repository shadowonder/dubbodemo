package com.wandm.dubbo.consumer.service;

import com.wandm.dubbo.proider.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestService {
    @Autowired
    private ProviderService providerService;
    @RequestMapping("")
    public String test(){
        return providerService.hi();
    }
}
