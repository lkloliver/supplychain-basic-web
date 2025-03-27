package org.hust.cse.supplychain.basic.modules.user.controller;

import jakarta.validation.Valid;
import org.hust.cse.supplychain.basic.common.response.ApiResponse;
import org.hust.cse.supplychain.basic.common.service.SmsService;
import org.hust.cse.supplychain.basic.common.utils.JwtUtil;
import org.hust.cse.supplychain.basic.modules.user.model.dto.LoginRequest;
import org.hust.cse.supplychain.basic.modules.user.model.dto.LoginResponse;
import org.hust.cse.supplychain.basic.modules.user.model.entity.User;
import org.hust.cse.supplychain.basic.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user;
        
        // 根据登录方式选择验证方法
        if (request.getUsername() != null && request.getPassword() != null) {
            // 用户名密码登录
            user = userService.findByUsername(request.getUsername());
            if (user == null) {
                return ApiResponse.error("用户名不存在");
            }
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ApiResponse.error("密码错误");
            }
        } else if (request.getPhone() != null && request.getCode() != null) {
            // 手机号验证码登录
            if (!smsService.verifyCode(request.getPhone(), request.getCode())) {
                return ApiResponse.error("验证码错误或已过期");
            }
            user = userService.findByPhone(request.getPhone());
            if (user == null) {
                return ApiResponse.error("该手机号未注册");
            }
        } else {
            return ApiResponse.error("登录参数错误");
        }

        // 生成token
        String token = jwtUtil.generateToken(
            user.getUsername(), 
            request.getIsAdmin()
        );

        // 返回登录响应
        return ApiResponse.success(LoginResponse.of(
            token,
            request.getVersion(),  // 版本信息仍然返回给前端
            request.getIsAdmin(),
            user
        ));
    }
} 