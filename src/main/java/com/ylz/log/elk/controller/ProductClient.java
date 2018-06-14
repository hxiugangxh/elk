package com.ylz.log.elk.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(name = "product", fallback = ProductClient.ProductClientFallback.class)
public interface ProductClient {

    @GetMapping("/product/msg")
    String msg();

    @Component
    static class ProductClientFallback implements ProductClient {

        @Override
        public String msg() {
            System.out.println("看见我，失败了");
            return "挂掉了";
        }
    }
}
