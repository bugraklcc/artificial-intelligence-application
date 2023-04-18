package com.example.yapayZeka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
@EnableEurekaServer
@SpringBootApplication
public class YapayZekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YapayZekaApplication.class, args);
	}

}
