package com.rest.goRest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;




@Configuration
@EnableAutoConfiguration
@EnableFeignClients(basePackages = {"com.rest.goRest.rest"})
public class AppConfig {

}
