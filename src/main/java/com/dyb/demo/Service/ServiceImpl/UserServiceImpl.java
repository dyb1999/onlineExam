package com.dyb.demo.Service.ServiceImpl;

import com.dyb.demo.Entity.User;
import com.dyb.demo.Mapper.UserMapper;
import com.dyb.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public List<User> selectByName(String name) {
        return userMapper.selectByName(name);
    }

    @Override
    public List<User> selectByNumer(String number) {
        return userMapper.selectByNumber(number);
    }

    @Override
    public Integer deleteByNumber(String number) {
        return userMapper.deleteByNumber(number);
    }

    @Override
    public Integer addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public Integer updateUserByNumber(User user) {
        return userMapper.updateUserByNumber(user);
    }

    @Override
    public User getUserByNumber(String number) {
        return userMapper.getUserByNumber(number);
    }
}
