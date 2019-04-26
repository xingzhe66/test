package com.dcits.comet.rpc.consumer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @FeignClient("sequence-provider")
    public interface TestClient {
        @RequestMapping(value = "/uid/def/def", method = RequestMethod.GET)
        Long getSonwKey();

        @RequestMapping(value = "/uid/load/{biztag}", method = RequestMethod.GET)
        Long getLoadKeyBybiztag(@PathVariable("biztag") String biztag);

        @RequestMapping(value = "/uid/redis/{biztag}", method = RequestMethod.GET)
        Long getRedisBybiztag(@PathVariable("biztag") String biztag);
    }

    @RestController
    public class TestController {

        @Autowired
        TestClient testClient;

        @GetMapping("/consumer")
        public String dc() {
            return String.valueOf(testClient.getSonwKey());
        }

        @GetMapping("/consumer1")
        public String dc1() {
            return String.valueOf(testClient.getLoadKeyBybiztag("cif.seqNo"));
        }

        @GetMapping("/consumer2")
        public String dc2() {
            return String.valueOf(testClient.getRedisBybiztag("redis"));
        }

    }
}
