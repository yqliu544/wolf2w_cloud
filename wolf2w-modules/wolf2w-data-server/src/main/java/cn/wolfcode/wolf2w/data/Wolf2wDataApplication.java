package cn.wolfcode.wolf2w.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("cn.wolfcode.wolf2w.data.mapper")
public class Wolf2wDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(Wolf2wDataApplication.class,args);
    }
}
