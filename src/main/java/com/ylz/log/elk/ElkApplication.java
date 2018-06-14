package com.ylz.log.elk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringCloudApplication
@MapperScan(basePackages = "com.ylz.**.mapper")
@ComponentScan(basePackages = {
		"com.ylz.log"
})
@EnableFeignClients(basePackages = {
		"com.ylz.log"
})
public class ElkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElkApplication.class, args);
	}
}
