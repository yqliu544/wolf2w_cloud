package cn.wolfcode.wolf2w.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("cn.wolfcode.wolf2w.user.mapper")
@SpringBootApplication
@ComponentScan(basePackages = {"cn.wolfcode.wolf2w.redis","cn.wolfcode.wolf2w.user"})
public class Wolf2wUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(Wolf2wUserApplication.class,args);
    }
}
