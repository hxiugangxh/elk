package com.ylz.log.elk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FeignController {

    @Autowired
    private ProductClient productClient;

    @GetMapping("/productFegin")
    public String productFegin() {

        log.info("feign");

        String msg = productClient.msg();

        return msg;
    }

}
