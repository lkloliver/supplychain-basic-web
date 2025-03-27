package org.hust.cse.supplychain.basic.modules.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hust.cse.supplychain.basic.modules.user.model.entity.User;

@Mapper
public interface UserMapper {
    
    /**
     * 通过用户名查询用户
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 通过手机号查询用户
     */
    User selectByPhone(@Param("phone") String phone);

    /**
     * 插入新用户
     */
    int insert(User user);
} 