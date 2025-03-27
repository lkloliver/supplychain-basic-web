package org.hust.cse.supplychain.basic.modules.user.controller;

import jakarta.validation.Valid;
import org.hust.cse.supplychain.basic.common.response.ApiResponse;
import org.hust.cse.supplychain.basic.common.service.SmsService;
import org.hust.cse.supplychain.basic.modules.user.model.dto.RegisterRequest;
import org.hust.cse.supplychain.basic.modules.user.model.entity.User;
import org.hust.cse.supplychain.basic.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    @PostMapping
    public ApiResponse<User> register(@Valid @RequestBody RegisterRequest request) {
        // 1. 验证两次密码是否一致
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ApiResponse.error("两次输入的密码不一致");
        }

        // 2. 验证手机验证码
        if (!smsService.verifyCode(request.getPhone(), request.getCode())) {
            return ApiResponse.error("验证码错误或已过期");
        }

        // 3. 检查用户名是否已存在
        if (userService.findByUsername(request.getUsername()) != null) {
            return ApiResponse.error("用户名已存在");
        }

        // 4. 检查手机号是否已注册
        if (userService.findByPhone(request.getPhone()) != null) {
            return ApiResponse.error("该手机号已注册");
        }

        // 5. 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());

        // 6. 保存用户
        User savedUser = userService.createUser(user);
        
        // 7. 清除密码后返回
        savedUser.setPassword(null);
        return ApiResponse.success(savedUser);
    }
} 