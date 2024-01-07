package cn.wolfcode.wolf2w.user.service.impl;

import cn.wolfcode.wolf2w.redis.core.exception.BusinessException;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.redis.core.utils.RedisCache;
import cn.wolfcode.wolf2w.user.redis.key.UserRedisKeyPrefix;
import cn.wolfcode.wolf2w.user.service.SmsService;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.tea.TeaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    @Value("${aliyun.dysms.templateCode}")
    private String templateCode;
    @Value("${aliyun.dysms.sign}")
    private String sign;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private Client smsClient;

    @Override
    public void registerSmsSend(String phone) {
        String code = this.generateVerifyCode("MATH", 6);
        try {
            this.send("18435681151",code);
        }catch (BusinessException re){
            throw re;
        }catch (Exception e) {
            e.printStackTrace();
        }
        UserRedisKeyPrefix keyPrefix = UserRedisKeyPrefix.USER_REGISTER_VERIFY_CODE_STRING;
        redisCache.setCacheObject(keyPrefix.fullKey(phone),code, keyPrefix.getTimeout(),keyPrefix.getUnit());
    }

    private void send(String phone,String code) throws Exception {
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName(sign)
                .setTemplateCode(templateCode)
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":\""+code+"\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();

            // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse response = smsClient.sendSmsWithOptions(sendSmsRequest, runtime);
        SendSmsResponseBody body = response.getBody();
        log.info("[短信服务]阿里云发送短信相应结果{}",body);
        if (!"ok".equalsIgnoreCase(body.code)){
            throw new BusinessException(R.CODE_SMS_ERROR,body.message);
        }

    }

    private String generateVerifyCode(String type, int len) {
        String code="";
        if ("MATH".equalsIgnoreCase(type)){
            Random random = new Random();
            for (int i = 0; i < len; i++) {
                code+=random.nextInt(10);
            }
        }else {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            code = uuid.substring(0, len);
        }

        log.info("【验证码】 生成验证码===》 type={},len={},code={}",type,len,code);
        return code;
    }
}
