package com.dcits.comet.rpc.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class TestRestful {

    private final Logger logger=Logger.getLogger(TestRestful.class.getName());

    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String home(){
//        ServiceInstance instance=client.getInstances();
//        logger.info("serviceId"+instance.getServiceId()+"host:post="+instance.getHost()+":"+instance.getPort());
        return "hello spring";
    }
}