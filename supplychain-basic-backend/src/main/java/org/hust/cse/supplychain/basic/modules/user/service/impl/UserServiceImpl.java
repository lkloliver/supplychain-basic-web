package org.hust.cse.supplychain.basic.modules.user.service.impl;

import org.hust.cse.supplychain.basic.modules.user.mapper.UserMapper;
import org.hust.cse.supplychain.basic.modules.user.model.entity.User;
import org.hust.cse.supplychain.basic.modules.user.service.UserService;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Order(2)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User findByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Override
    public User createUser(User user) {
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 插入用户
        userMapper.insert(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        // TODO: 实现更新用户
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        // TODO: 实现删除用户
    }
} 