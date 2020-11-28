package com.vv.personal.twm.render;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Vivek
 * @since 17/11/20
 */
@EnableSwagger2
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class RenderingServer {
    public static void main(String[] args) {
        SpringApplication.run(RenderingServer.class, args);
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }
}
