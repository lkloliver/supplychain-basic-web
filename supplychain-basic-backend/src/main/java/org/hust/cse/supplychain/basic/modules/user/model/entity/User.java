/**
 * @author: zyk
 * @date: 2025-03-27
 * @description: 用户实体类
 */
package org.hust.cse.supplychain.basic.modules.user.model.entity;

import lombok.Data;

@Data
public class User {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
} 