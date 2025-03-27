package org.hust.cse.supplychain.basic.modules.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {
    
    private String username;  // 用户名登录时使用
    
    private String password;  // 用户名登录时使用
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;    // 手机号登录时使用
    
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须是6位数字")
    private String code;     // 手机号登录时使用

    @Pattern(regexp = "^(BASE|NON_BASE)$", message = "版本类型必须是 BASE 或 NON_BASE")
    private String version;  // BASE: 基地版, NON_BASE: 非基地版

    private Boolean isAdmin = false;  // 是否是管理员登录
} 