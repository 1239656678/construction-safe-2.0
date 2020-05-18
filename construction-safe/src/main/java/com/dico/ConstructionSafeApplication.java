package com.dico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableHystrix
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class ConstructionSafeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConstructionSafeApplication.class, args);
    }

//    /**
//     * 分布式事务bean注入
//     */
//    @Bean
//    public GlobalTransactionScanner setGlobalTransactionScanner(){
//        return new GlobalTransactionScanner("construction-safe", "my_test_tx_group");
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DruidDataSource druidDataSource() {
//        DruidDataSource druidDataSource = new DruidDataSource();
//        return druidDataSource;
//    }
//
//    @Primary
//    @Bean("dataSource")
//    public DataSourceProxy dataSource(DruidDataSource druidDataSource) {
//        return new DataSourceProxy(druidDataSource);
//    }
}
