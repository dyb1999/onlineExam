package com.dyb.demo.Configuration;

import com.dyb.demo.Shiro.CustomRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.adapter.DefaultAdvisorAdapterRegistry;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class shiroConfig {

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean(name = "getCustomRealm")
    public CustomRealm myShiroRealm() {
        //获取realm认证规则
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
    }

    @Bean(name = "getSecurityManager")
    public SecurityManager securityManager(@Qualifier("getCustomRealm") CustomRealm customRealm) {
        //权限管理，将自定义Realm添加到SecurityManager里，并通过@bean注解注册到spring容器中
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("getSecurityManager") SecurityManager SecurityManager) {
        //Filter过滤器工厂，设置过滤和跳转条件
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(SecurityManager);
        Map<String, String> map = new HashMap<>();

        //开放登录接口
        map.put("/user/login", "anon");
        //开放注册接口
        map.put("/user/register", "anon");
        map.put("/user/add", "anon");
        //登出界面
        map.put("/user/logout", "logout");
        //拦截所有路径
        map.put("/**", "authc");

        shiroFilterFactoryBean.setLoginUrl("/user/login");
//        shiroFilterFactoryBean.setSuccessUrl("/user/list");
        shiroFilterFactoryBean.setUnauthorizedUrl("/user/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;

    }
}
