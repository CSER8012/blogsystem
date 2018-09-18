package com.imooc.demoblog.service;

import com.imooc.demoblog.entity.User;

public interface UserService {

    User checkUser(String username, String password);
    boolean containsUser(Long id);
    void addUser(User user);
    User findByUsername(String username);
}
