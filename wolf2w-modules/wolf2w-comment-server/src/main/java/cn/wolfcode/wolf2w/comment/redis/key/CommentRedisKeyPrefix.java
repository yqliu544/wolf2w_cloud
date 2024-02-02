package cn.wolfcode.wolf2w.comment.redis.key;

import cn.wolfcode.wolf2w.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class CommentRedisKeyPrefix extends BaseKeyPrefix {
    public static  final  CommentRedisKeyPrefix STRATEGIES_STAT_DATA_MAP=new CommentRedisKeyPrefix("STRATEGIES:STST:DATA");
    public CommentRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public CommentRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }
}
