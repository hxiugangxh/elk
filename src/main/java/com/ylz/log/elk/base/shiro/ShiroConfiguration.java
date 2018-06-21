/*
package com.ylz.log.elk.base.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

*/
/**
 * 这是Shiro 配置类：
 * -----------------------
 * （1）需要配置ShiroFilterFactory :ShiroFilterFactoryBean
 * （2）配置SecurityManager
 *//*

@Configuration //这是配置类.
public class ShiroConfiguration {


    */
/**
     * 定义shiro Filter 工厂类.
     *
     * @param securityManager
     * @return
     *//*

    @Bean //注入shiroFilter.
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

		*/
/*
         * 1、定义ShiroFactoryBean.
		 * 2、设置SecurityManager;
		 * 3、配置拦截器+配置登录和登录成功的地址.
		 * 4、返回ShiroFactoryBean.
		 *//*

        //1、定义ShiroFactoryBean.
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //2、设置SecurityManager;
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap<String, Filter>();
        CustomFormAuthticationFilter authticationFilter = new CustomFormAuthticationFilter();
        filters.put("authc", authticationFilter);
        shiroFilterFactoryBean.setFilters(filters);

        //3、配置拦截器.: 使用Map进行配置:LinkedHashMap ，LinkedHashMap是有序的, shiro会根据添加的顺序进行拦截.
        Map<String, String> filterChainMap = new LinkedHashMap<String, String>();

        //配置退出. 过滤器：logout,这个由shiro进行实现的.
        filterChainMap.put("/logout", "logout");

        //配置记住我 认证通过的才可以访问. //userInfo/userAdd : 一旦重新启动，仍然需要重新登录的.
        filterChainMap.put("/index", "user");
        //filterChainMap.put("/","user");

        //允许favicon.ico可以匿名访问（anon）
        filterChainMap.put("/favicon.ico", "anon");
        filterChainMap.put("/captcha", "anon");
        filterChainMap.put("/js/**", "anon");//可匿名访问到js文件
        filterChainMap.put("/css/**", "anon");
        filterChainMap.put("/img/**", "anon");

        //authc:所有的URL都必须认证通过才可以访问.
//        filterChainMap.put("/**", "authc");
        filterChainMap.put("/**", "anon");

        //设置默认登录的URL.
        shiroFilterFactoryBean.setLoginUrl("/login");
        //设置成功之后要跳转的链接.
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面.
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);

        //4、返回ShiroFactoryBean.
        return shiroFilterFactoryBean;
    }

    */
/**
     * 定义Shiro的安全管理器.
     *
     * @return
     *//*

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //设置自定义的realm
        securityManager.setRealm(myShiroRealm());

        //注入缓存管理器.
        securityManager.setCacheManager(ehCacheManager());

        //配置记住我.
        securityManager.setRememberMeManager(cookieRememberMeManager());

        return securityManager;
    }

    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    */
/**
     * 密码加密算法.
     *
     * @return
     *//*

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {

        //默认的.
        //HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        MyHashedCredentialsMatcher hashedCredentialsMatcher = new MyHashedCredentialsMatcher(ehCacheManager());

        hashedCredentialsMatcher.setHashAlgorithmName("md5");//加密算法.
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数.
        return hashedCredentialsMatcher;
    }


    */
/**
     * 开启Shiro aop注解支持.
     *//*

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor attributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        attributeSourceAdvisor.setSecurityManager(securityManager);//设置安全管理器
        return attributeSourceAdvisor;
    }


    */
/**
     * 注入Ehcache缓存.
     *//*

    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        //配置缓存文件.
        ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
        return ehCacheManager;
    }


    */
/**
     * 配置cookie对象. -- 记住我cookie
     *//*

    @Bean
    public SimpleCookie rememberMeCookie() {
        //cookie的名称，也即是 前端 checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //可选： 配置cookie的生效时间，单位是秒. 60*60*24 = 1天.
        simpleCookie.setMaxAge(60 * 60 * 24);
        return simpleCookie;
    }


    */
/**
     * cookie的管理对象.
     *//*

    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        //需要管理我们的cookie对象.
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    */
/**
     * 是shiro - thymeleaf 解析.
     *
     * @return
     *//*

    @Bean
    public ShiroDialect shiroDialect() {
        ShiroDialect shiroDialect = new ShiroDialect();
        return shiroDialect;
    }


    */
/**
     * 验证码生成器;
     *
     * @return
     *//*

    @Bean
    public Producer producer() {
        System.out.println("ShiroConfiguration.captchaProducer()");
        DefaultKaptcha producer = new DefaultKaptcha();
        Properties properties = new Properties();
		
		*/
/*
kaptcha.border  是否有边框  默认为true  我们可以自己设置yes，no  
kaptcha.border.color   边框颜色   默认为Color.BLACK  
kaptcha.border.thickness  边框粗细度  默认为1  
kaptcha.producer.impl   验证码生成器  默认为DefaultKaptcha  
kaptcha.textproducer.impl   验证码文本生成器  默认为DefaultTextCreator  
kaptcha.textproducer.char.string   验证码文本字符内容范围  默认为abcde2345678gfynmnpwx  
kaptcha.textproducer.char.length   验证码文本字符长度  默认为5  
kaptcha.textproducer.font.names    验证码文本字体样式  默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)  
kaptcha.textproducer.font.size   验证码文本字符大小  默认为40  
kaptcha.textproducer.font.color  验证码文本字符颜色  默认为Color.BLACK  
kaptcha.textproducer.char.space  验证码文本字符间距  默认为2  
kaptcha.noise.impl    验证码噪点生成对象  默认为DefaultNoise  
kaptcha.noise.color   验证码噪点颜色   默认为Color.BLACK  
kaptcha.obscurificator.impl   验证码样式引擎  默认为WaterRipple  
kaptcha.word.impl   验证码文本字符渲染   默认为DefaultWordRenderer  
kaptcha.background.impl   验证码背景生成器   默认为DefaultBackground  
kaptcha.background.clear.from   验证码背景颜色渐进   默认为Color.LIGHT_GRAY  
kaptcha.background.clear.to   验证码背景颜色渐进   默认为Color.WHITE  
kaptcha.image.width   验证码图片宽度  默认为200  
kaptcha.image.height  验证码图片高度  默认为50 
		 *//*


        // 是否有边框  默认为true  我们可以自己设置yes，no
        properties.put("kaptcha.border", "yes");

        //边框颜色   默认为Color.BLACK
        properties.put("kaptcha.border.color", "105,179,90");

        //字体颜色;
        properties.put("kaptcha.textproducer.font.color", "blue");

        //验证码样式引擎  默认为WaterRipple
        properties.put("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");

        // 验证码图片宽度  默认为200
        properties.put("kaptcha.image.width", "145");

        //验证码图片高度  默认为50
        properties.put("kaptcha.image.height", "45");

        //验证码文本字符大小  默认为40
        properties.put("kaptcha.textproducer.font.size", "45");

        //存放在session中的key;
        properties.put("kaptcha.session.key", "code");

        //产生字符的长度
        properties.put("kaptcha.textproducer.char.length", "4");

        //文本字符字体
        properties.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");

        Config config = new Config(properties);
        producer.setConfig(config);
        return producer;
    }

}
*/
