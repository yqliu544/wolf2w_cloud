package cn.wolfcode.wolf2w.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Wolf2wSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(Wolf2wSearchApplication.class,args);
    }
}
