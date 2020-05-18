package com.dico.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger2配置类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: Swagger2Config
 * 创建时间: 2018/12/12
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        //添加head参数start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        //添加head参数end

        // 创建API基本信息
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()// 发布时扫描该包cn.dico.modules.app
                .apis(RequestHandlerSelectors.basePackage("com.dico"))// 扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外
                .paths(PathSelectors.any())
                .build().globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        // 创建API的基本信息，这些信息会在Swagger UI中进行显示
        Contact contact = new Contact("Gaodl", "http://www.diconet.cn", "1966811719@qq.com");
        return new ApiInfoBuilder()
                .title("模块接口")// API 标题
                .description("模块接口")// API描述
                .version("1.0")// 版本号
                .contact(contact)
                .build();
    }
}
