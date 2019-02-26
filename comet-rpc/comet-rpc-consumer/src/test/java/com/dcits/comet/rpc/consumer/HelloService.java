package com.dcits.comet.rpc.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloService {
    @Autowired ServiceBClient serviceBClient;

    @RequestMapping("/h")
    public String getHelloContent() {
        return serviceBClient.hello();
    }
}