package com.bx.gis.config;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2019/2/18
 * @Version V1.0
 **/
@Configuration
public class ShiroConfiguration {
    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是会报错的，因为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     * Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "AuthorizingRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public ShiroRealm shiroRealm() {
        ShiroRealm realm = new ShiroRealm();
        return realm;
    }

    @Bean(name = "ehCacheManager")
    @DependsOn("lifecycleBeanPostProcessor")
    public EhCacheManager ehCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        return ehCacheManager;
    }

    /**
     * cookie对象;
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }
    /**
     * cookie管理对象;记住我功能
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    @Bean(name = "defaultWebSecurityManager")
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        securityManager.setCacheManager(ehCacheManager());//用户授权/认证信息Cache, 采用EhCache 缓存
        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        Map<String, String> filterChainDefinitionManager = new LinkedHashMap<String, String>();
        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了uo
        filterChainDefinitionManager.put("/logout", "logout");
        filterChainDefinitionManager.put("/register", "anon"); //用户注册
        //配置记住我或认证通过可以访问的地址
        filterChainDefinitionManager.put("/", "user");
        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访
        filterChainDefinitionManager.put("/user/**", "authc,roles[user]");
        filterChainDefinitionManager.put("/admin/**", "authc,roles[admin]");
        filterChainDefinitionManager.put("/login", "anon"); //登录页
        filterChainDefinitionManager.put("/403", "anon");
        filterChainDefinitionManager.put("/forget", "anon"); //忘记密码
        filterChainDefinitionManager.put("/selectUserInfo", "anon"); //用户数据请求
        filterChainDefinitionManager.put("/success", "anon"); //验证成功后登录页面
        /*地图请求 Start*/
        /*filterChainDefinitionManager.put("/querySceneryInfo", "anon"); //验证成功后登录页面
        filterChainDefinitionManager.put("/uploadImages", "anon"); //验证成功后登录页面
        filterChainDefinitionManager.put("/addNewSceneryInfo", "anon"); //验证成功后登录页面
        filterChainDefinitionManager.put("/querySceneryInfoByCode", "anon"); //验证成功后登录页面
        filterChainDefinitionManager.put("/deleteNewSceneryInfo", "anon"); //验证成功后登录页面
        filterChainDefinitionManager.put("/queryNewSceneryPartInfo", "anon"); //验证成功后登录页面
        filterChainDefinitionManager.put("/querySceneryEntranceInfos", "anon"); //验证成功后登录页面
        filterChainDefinitionManager.put("/addPointlineInfos", "anon"); //验证成功后登录页面
        filterChainDefinitionManager.put("/querySceneryTrackInfos", "anon"); //验证成功后登录页面*/
        /*地图请求 End*/
        filterChainDefinitionManager.put("/img/**", "anon"); //允许匿名访问图片
        filterChainDefinitionManager.put("/css/**", "anon"); //css样式
        filterChainDefinitionManager.put("/js/**",  "anon");
        filterChainDefinitionManager.put("/webjars/**",  "anon");
        filterChainDefinitionManager.put("/check", "anon"); //校验用户是否存在
        filterChainDefinitionManager.put("/ajaxLogin", "anon"); //登录
        filterChainDefinitionManager.put("/static/**",  "anon");
//        filterChainDefinitionManager.put("/**",  "authc,roles[user]");//其他资源全部拦截
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.html"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/success");
        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        return shiroFilterFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }

}
