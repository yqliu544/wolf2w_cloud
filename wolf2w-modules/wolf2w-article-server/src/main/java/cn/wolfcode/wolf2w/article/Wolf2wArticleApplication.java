package cn.wolfcode.wolf2w.article;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:58
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan("cn.wolfcode.wolf2w.article.mapper")
public class Wolf2wArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(Wolf2wArticleApplication.class,args);
    }
}
