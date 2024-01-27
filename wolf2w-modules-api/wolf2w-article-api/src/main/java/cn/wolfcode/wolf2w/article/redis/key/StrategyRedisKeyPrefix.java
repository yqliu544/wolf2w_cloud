package cn.wolfcode.wolf2w.article.redis.key;

import cn.wolfcode.wolf2w.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class StrategyRedisKeyPrefix extends BaseKeyPrefix {

    public static  final  StrategyRedisKeyPrefix STRATEGIES_STAT_DATA_MAP=new StrategyRedisKeyPrefix("STRATEGIES:STST:DATA");
    public StrategyRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public StrategyRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }
}
