package cn.wolfcode.wolf2w.auth.config;

import cn.wolfcode.wolf2w.auth.interceptor.LoginInterceptor;
import cn.wolfcode.wolf2w.redis.core.utils.RedisCache;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WebConfig.class)
@EnableConfigurationProperties(JwtProperties.class)
public class Wolf2wJwtAutoConfiguration {
    @Bean
    public LoginInterceptor loginInterceptor(RedisCache redisCache,JwtProperties jwtProperties){
        return new LoginInterceptor(redisCache,jwtProperties);
    }

}
