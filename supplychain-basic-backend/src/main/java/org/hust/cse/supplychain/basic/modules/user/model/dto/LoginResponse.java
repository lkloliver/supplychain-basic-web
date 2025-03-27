package org.hust.cse.supplychain.basic.modules.user.model.dto;

import lombok.Data;
import org.hust.cse.supplychain.basic.modules.user.model.entity.User;

@Data
public class LoginResponse {
    private String token;           // JWT token
    private String version;         // 版本类型：BASE/NON_BASE
    private Boolean isAdmin;        // 是否是管理员
    private User user;             // 用户信息（不包含密码）
    
    public static LoginResponse of(String token, String version, Boolean isAdmin, User user) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setVersion(version);
        response.setIsAdmin(isAdmin);
        if (user != null) {
            user.setPassword(null); // 清除密码
        }
        response.setUser(user);
        return response;
    }
} 