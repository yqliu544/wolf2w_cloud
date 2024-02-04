package cn.wolfcode.wolf2w.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"cn.wolfcode.wolf2w.redis","cn.wolfcode.wolf2w.search"})
public class Wolf2wSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(Wolf2wSearchApplication.class,args);
    }
}
