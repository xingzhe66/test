package com.dcits.comet.rpc.consumer;

import com.dcits.comet.rpc.consumer.annotation.RpcClient;
import com.dcits.comet.rpc.consumer.annotation.RpcMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@RpcClient(name = "hello-service", fallback = ServiceBClient.ServiceBClientFallback.class)
public interface ServiceBClient {

    @RpcMethod(value = "/hello")
    String hello();

    @Component
    class ServiceBClientFallback implements ServiceBClient {

        private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBClientFallback.class);

        @Override
        public String hello() {
            LOGGER.info("异常发生，进入fallback方法");
            return "SERVICE B FAILED! - FALLING BACK";
        }
    }
}