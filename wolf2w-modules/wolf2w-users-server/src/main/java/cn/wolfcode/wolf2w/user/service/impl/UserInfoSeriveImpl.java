package cn.wolfcode.wolf2w.user.service.impl;

import cn.wolfcode.wolf2w.auth.config.JwtProperties;
import cn.wolfcode.wolf2w.redis.core.exception.BusinessException;
import cn.wolfcode.wolf2w.redis.core.utils.Md5Utils;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.redis.core.utils.RedisCache;
import cn.wolfcode.wolf2w.user.mapper.UserInfoMapper;
import cn.wolfcode.wolf2w.user.redis.key.UserRedisKeyPrefix;
import cn.wolfcode.wolf2w.user.service.UserInfoService;
import cn.wolfcode.wolf2w.user.domain.UserInfo;
import cn.wolfcode.wolf2w.user.vo.LoginUser;
import cn.wolfcode.wolf2w.user.vo.RegisterRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoSeriveImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public UserInfo findByPhone(String phone) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<UserInfo>().eq("phone",phone);
        return getOne(wrapper);
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        UserInfo userInfo = this.findByPhone(registerRequest.getPhone());
        if (userInfo!=null){
            throw new BusinessException(R.CODE_REGISTER_ERROR,"手机号已存在，请不要重复注册");
        }
        String fullKey = UserRedisKeyPrefix.USER_REGISTER_VERIFY_CODE_STRING.fullKey(registerRequest.getPhone());
        String code = redisCache.getCacheObject(fullKey);
        if (!registerRequest.getVerifyCode().equalsIgnoreCase(code)){
            throw new BusinessException(R.CODE_REGISTER_ERROR,"验证码错误");
        }
        redisCache.deleteObject(fullKey);
        userInfo=this.buildUserInfo(registerRequest);
        super.save(userInfo);
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        UserInfo userInfo = this.findByPhone(username);
        if (userInfo==null){
            throw new BusinessException(500401,"用户名或密码错误");
        }
        String encryptPassword = Md5Utils.getMD5(password);
        if (!encryptPassword.equalsIgnoreCase(userInfo.getPassword())){
            throw new BusinessException(500401,"用户名或密码错误");
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Map<String, Object> payload = new HashMap<>();
        payload.put(LoginUser.LOGIN_USER_REDIS_UUID,uuid);
        String jwtToken = Jwts.builder().addClaims(payload).signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret()).compact();
        payload.clear();
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(userInfo,loginUser);
        long currentTime = System.currentTimeMillis();
        loginUser.setLoginTime(currentTime);
        loginUser.setExpireTime(currentTime+(30*60000));
        payload.put("token",jwtToken);
        payload.put("user",loginUser);

        UserRedisKeyPrefix userLoginInfoString = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING;
        userLoginInfoString.setTimeout(currentTime+(30*60000));
        userLoginInfoString.setUnit(TimeUnit.MINUTES);
        redisCache.setCacheObject(userLoginInfoString,loginUser,uuid);

        return payload;
    }

    private UserInfo buildUserInfo(RegisterRequest registerRequest) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(registerRequest,userInfo);
        userInfo.setInfo("这个人很懒，什么都没写");
        userInfo.setState(UserInfo.STATE_NORMAL);
        String encryptPassword = Md5Utils.getMD5(userInfo.getPassword());
        userInfo.setPassword(encryptPassword);
        return userInfo;
    }
}
