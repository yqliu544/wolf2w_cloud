package cn.wolfcode.wolf2w.article.listener;

import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.redis.key.StrategyRedisKeyPrefix;
import cn.wolfcode.wolf2w.article.service.StrategyService;
import cn.wolfcode.wolf2w.redis.core.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RedisStatDataInitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private StrategyService strategyService;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        if (AnnotationConfigServletWebServerApplicationContext.class==applicationContext.getClass()){
            System.out.println("--------------------容器启动完成，执行初始化数据--------------------");
            List<Strategy> list = strategyService.list();
            for (Strategy strategy : list) {
                String fullKey = StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(strategy.getId() + "");
                Boolean exists = redisCache.hasKey(fullKey);
                if (!exists) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("viewnum",strategy.getViewnum());
                    map.put("favornum",strategy.getFavornum());
                    map.put("replynum",strategy.getReplynum());
                    map.put("sharenum",strategy.getSharenum());
                    map.put("thumbsupnum",strategy.getThumbsupnum());
                    redisCache.setCacheMap(fullKey,map);
                }
            }

            System.out.println("--------------------数据初始化完成--------------------");
        }
    }
}
