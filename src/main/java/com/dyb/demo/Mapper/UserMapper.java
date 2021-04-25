package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {
    //返回所有的用户信息
    @Select("select id,name,realname,password,role,age,gender,loginNumber,major,grade,myclass,email,phone from t_user")
    List<User> findAll();

    //根据学号查找用户
    @Select("select id,name,realname,password,role,age,gender,loginNumber,major,grade,myclass,email,phone from t_user where loginNumber=#{number}")
    User getUserByNumber(String number);

    //根据用户名做模糊查询
    @Select("select * from t_user where realname like '%${name}%'")
    List<User> selectByName(String name);

    //根据学号或者工号做模糊查询
    @Select("select * from t_user where loginNumber like '%${number}%'")
    List<User> selectByNumber(String number);

    //删除用户
    @Delete("delete from t_user where loginNumber=#{number}")
    Integer deleteByNumber(String number);

    //添加用户
    @Insert("insert into t_user(name,realname,password,role,age,gender,loginNumber,major,grade,myclass,email,phone) values (#{name},#{realname},#{password},#{role},#{age},#{gender},#{loginNumber},#{major},#{grade},#{myclass},#{email},#{phone})")
    Integer addUser(User user);

    //根据学号修改用户信息
    @Update("update t_user set name=#{name},realname=#{realname},password=#{password},role=#{role},age=#{role},gender=#{gender},loginNumber=#{loginNumber},major=#{major},grade=#{grade},myclass=#{myclass},email=#{email},phone=#{phone} where loginNumber=#{loginNumber}")
    Integer updateUserByNumber(User user);
}
