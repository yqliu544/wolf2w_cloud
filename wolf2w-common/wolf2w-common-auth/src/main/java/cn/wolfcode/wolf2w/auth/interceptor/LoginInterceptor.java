package cn.wolfcode.wolf2w.auth.interceptor;

import cn.wolfcode.wolf2w.auth.anno.RequireLogin;
import cn.wolfcode.wolf2w.auth.config.JwtProperties;
import cn.wolfcode.wolf2w.auth.util.AuthenticationUtils;
import cn.wolfcode.wolf2w.redis.core.exception.BusinessException;
import cn.wolfcode.wolf2w.redis.core.utils.RedisCache;
import cn.wolfcode.wolf2w.user.redis.key.UserRedisKeyPrefix;
import cn.wolfcode.wolf2w.user.vo.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;


public class LoginInterceptor implements HandlerInterceptor {

    private final RedisCache redisCache;

    private final JwtProperties jwtProperties;

    public LoginInterceptor(RedisCache redisCache, JwtProperties jwtProperties) {
        this.redisCache = redisCache;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断handler 是否是HandlerMethod实例，如果不是，直接放行
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod hm= (HandlerMethod) handler;
        Class<?> controllerClass = hm.getBeanType();
        RequireLogin classAnnotation = controllerClass.getAnnotation(RequireLogin.class);
        RequireLogin methodAnnotation = hm.getMethodAnnotation(RequireLogin.class);
        if (classAnnotation==null&&methodAnnotation==null){
            return true;
        }

        String token = request.getHeader(LoginUser.TOKEN_HEADER);
        try {
            Jws<Claims> jwt = Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token);
            Claims claims = jwt.getBody();
            String uuid = (String) claims.get(LoginUser.LOGIN_USER_REDIS_UUID);
            LoginUser loginUser = redisCache.getCacheObject(UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(uuid));
            long loginTime;
            if (loginUser==null){
                throw new BusinessException(401,"token已过期");
            }else if ((loginUser.getExpireTime()-(loginTime=System.currentTimeMillis()))<=LoginUser.TWENTY_MILLISECONDS){
                //用户过期剩余小于20分中，就刷新该用户的过期时间
                loginUser.setLoginTime(loginTime);
                long expireTime=loginTime+(jwtProperties.getExpireTime()*60*1000);
                loginUser.setExpireTime(expireTime);
                redisCache.setCacheObject(UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(uuid),loginUser,expireTime, TimeUnit.MILLISECONDS);
            }

        } catch (Exception e) {
            throw new BusinessException(401,"用户未认证");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        AuthenticationUtils.removeUser();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
