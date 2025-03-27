package org.hust.cse.supplychain.basic.common.service.impl;

import org.hust.cse.supplychain.basic.common.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 模拟的短信服务实现
 * 实际项目中应该替换为真实的短信服务
 */
@Service
public class MockSmsServiceImpl implements SmsService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    private final Random random = new Random();
    
    // Redis中验证码的key前缀
    private static final String CODE_PREFIX = "sms:code:";
    // 验证码有效期（分钟）
    private static final long CODE_EXPIRE_MINUTES = 5;

    @Override
    public String sendVerificationCode(String phone) {
        // 生成6位数字验证码
        String code = String.format("%06d", random.nextInt(1000000));
        // 将验证码保存到Redis，设置5分钟过期
        String key = CODE_PREFIX + phone;
        redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        // 模拟发送短信
        System.out.println("向手机号" + phone + "发送验证码：" + code);
        return code;
    }

    @Override
    public boolean verifyCode(String phone, String code) {
        String key = CODE_PREFIX + phone;
        // 从Redis中获取验证码
        String cachedCode = redisTemplate.opsForValue().get(key);
        if (cachedCode != null && cachedCode.equals(code)) {
            // 验证成功后删除验证码
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
} 