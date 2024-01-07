package cn.wolfcode.wolf2w.user.redis.key;

import cn.wolfcode.wolf2w.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class UserRedisKeyPrefix extends BaseKeyPrefix {
    public static final UserRedisKeyPrefix USER_REGISTER_VERIFY_CODE_STRING=new UserRedisKeyPrefix("USERS:REGISTER:VERIFY_CODE",30l,TimeUnit.MINUTES);
    public static final UserRedisKeyPrefix USER_LOGIN_INFO_STRING=new UserRedisKeyPrefix("USERS:LOGIN:INFO");

    public UserRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public UserRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }
}
