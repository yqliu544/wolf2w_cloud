package cn.wolfcode.wolf2w.article.config;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {
    @Bean
    public ThreadPoolExecutor businessThreadPoolExecutor(){
        return new ThreadPoolExecutor(10,50,10, TimeUnit.SECONDS,new LinkedBlockingDeque<>(100));
    }

    @PostConstruct
    public void init(){

    }
}
