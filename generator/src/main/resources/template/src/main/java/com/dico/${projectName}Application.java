package com.dico;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableHystrix
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories
public class ${projectNameUpperName}Application{

    public static void main(String[]args){
        SpringApplication.run(${projectNameUpperName}Application.class,args);
    }

}

