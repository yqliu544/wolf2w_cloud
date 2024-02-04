package cn.wolfcode.wolf2w.gateway;

import cn.wolfcode.wolf2w.redis.core.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,value = SwaggerConfig.class)})
public class Wolf2wGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(Wolf2wGatewayApplication.class,args);
    }
}
