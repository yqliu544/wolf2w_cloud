package cn.wolfcode.wolf2w.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.wolfcode.wolf2w.redis","cn.wolfcode.wolf2w.comment"})
public class Wolf2wCommentApplication {
    public static void main(String[] args) {
        SpringApplication.run(Wolf2wCommentApplication.class,args);
    }
}
