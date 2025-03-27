package org.hust.cse.supplychain.basic.modules.user.service;

import org.hust.cse.supplychain.basic.modules.user.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    User findByPhone(String phone);
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
} 