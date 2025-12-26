package com.network.simulation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j API文档配置
 *
 * @author Network Simulation Team
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("网络模拟系统API文档")
                        .description("网络数据传输与封装模拟系统接口文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Network Simulation Team")
                                .email("support@example.com")));
    }
}
