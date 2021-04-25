package com.dyb.demo.Service;

import com.dyb.demo.Entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    //获取所有用户信息
    List<User> findAll();

    //根据用户名做模糊查询
    List<User> selectByName(String name);

    //根据工号/学号做模糊查询
    List<User> selectByNumer(String number);

    //根据学号/工号删除用户
    Integer deleteByNumber(String number);

    //添加用户
    Integer addUser(User user);

    //修改用户信息
    Integer updateUserByNumber(User user);

    //根据学号查找用户
    User getUserByNumber(String number);

}
