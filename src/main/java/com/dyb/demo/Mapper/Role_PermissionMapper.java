package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.Role_Permission;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface Role_PermissionMapper {

    @Select("select * from role_permission")
    List<Role_Permission> findAll();

    @Select("select * from role_permission where role=#{role}")
    List<Role_Permission> findByRole(Integer role);

    @Delete("delete from role_permission where role=#{role} and permission=#{permission}")
    Integer deleteByRolePermission(Integer role, String permission);

    @Insert("insert into role_permission(role, permission) values (#{role}, #{permission})")
    Integer addPermissionToRole(Integer role, String permission);

    @Update("update role_permission set role=#{role}, permission=#{permission}")
    Integer updateRolePermission(Integer role, String permission);
}
