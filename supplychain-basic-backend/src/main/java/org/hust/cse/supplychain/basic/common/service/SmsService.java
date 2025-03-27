/**
 * @author: zyk
 * @date: 2025-03-27
 * @description: 短信服务接口
 */
package org.hust.cse.supplychain.basic.common.service;

public interface SmsService {
    /**
     * 发送验证码
     * @param phone 手机号
     * @return 验证码
     */
    String sendVerificationCode(String phone);

    /**
     * 验证验证码
     * @param phone 手机号
     * @param code 验证码
     * @return 是否验证通过
     */
    boolean verifyCode(String phone, String code);
} 