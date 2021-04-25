package com.dyb.demo.Shiro;

import com.dyb.demo.Entity.Role_Permission;
import com.dyb.demo.Entity.User;
import com.dyb.demo.Mapper.Role_PermissionMapper;
import com.dyb.demo.Service.UserService;
import org.apache.poi.util.StringUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public class CustomRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    @Autowired
    Role_PermissionMapper role_permissionMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String loginNumber = (String) principalCollection.getPrimaryPrincipal();
        User user = userService.getUserByNumber(loginNumber);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole(String.valueOf(user.getRole()));
        for (Role_Permission rolePermission : role_permissionMapper.findByRole(user.getRole())) {
            simpleAuthorizationInfo.addStringPermission(rolePermission.getPermission());
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (StringUtils.isEmpty(authenticationToken.getPrincipal())) {
            return null;
        }
        //获取用户信息
        String loginNumber = authenticationToken.getPrincipal().toString();
        User user = userService.getUserByNumber(loginNumber);
        if (user == null) {
            //抛出异常
            return null;
        } else {
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(loginNumber, user.getPassword(), getName());
            return simpleAuthenticationInfo;
        }
    }
}
