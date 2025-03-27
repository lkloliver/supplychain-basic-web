package org.hust.cse.supplychain.basic.modules.user.controller;

import org.hust.cse.supplychain.basic.common.response.ApiResponse;
import org.hust.cse.supplychain.basic.common.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class SmsController {

    @Autowired
    private SmsService smsService;

    @GetMapping("/code")
    public ApiResponse<String> sendVerificationCode(@RequestParam String phone) {
        // 实际项目中应该先校验手机号格式
        String code = smsService.sendVerificationCode(phone);
        // 在开发环境中返回验证码，生产环境中不应该返回
        return ApiResponse.success(code);
    }
} 