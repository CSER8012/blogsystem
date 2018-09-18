package com.imooc.demoblog.service.serviceImpl;

import com.imooc.demoblog.dao.UserRepository;
import com.imooc.demoblog.entity.User;
import com.imooc.demoblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {

        User user = userRepository.findByUsernameAndPassword(username,password);
        return user;
    }

    @Override
    public boolean containsUser(Long id) {
        if(userRepository.getOne(id) != null)
            return true;

        return false;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}
