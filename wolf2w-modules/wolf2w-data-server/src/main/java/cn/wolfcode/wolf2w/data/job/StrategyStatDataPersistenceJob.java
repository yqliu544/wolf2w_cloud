package cn.wolfcode.wolf2w.data.job;

import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.redis.key.StrategyRedisKeyPrefix;
import cn.wolfcode.wolf2w.data.service.StragegyService;
import cn.wolfcode.wolf2w.redis.core.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@Component
public class StrategyStatDataPersistenceJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private StragegyService stragegyService;

    @Scheduled(cron = "0 */10 * * * *")
    public void task(){
        Set<Integer> list = redisCache.zsetRevrange(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 0, Integer.MAX_VALUE);
        ArrayList<Strategy> updateList = new ArrayList<>();
        for (Integer id : list) {
            Map<String, Object> map = redisCache.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
            Strategy strategy = new Strategy();
            strategy.setViewnum((Integer) map.get("viewnum"));
            strategy.setSharenum((Integer) map.get("sharenum"));
            strategy.setFavornum((Integer) map.get("favornum"));
            strategy.setReplynum((Integer) map.get("replynum"));
            strategy.setThumbsupnum((Integer) map.get("thumbsupnum"));
            strategy.setId(id.longValue());
            updateList.add(strategy);
        }
        stragegyService.updateBatchById(updateList,100);

        redisCache.zsetRemoveRange(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 0, Integer.MAX_VALUE);
    }
}
