package com.ylz.log.elk.base.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义凭证匹配器，用于密码次数的控制.
 * ---------------------------------
 * 编码思路（使用缓存记录用户的密码输入次数）：
 * -------
 * （1）注入缓存对象；
 * （2）获取到当前登录的用户账号username;
 * （3）获取当前账号的登录次数，cache<string,AtomicInteger> ,AtomicInteger:是线程安全的，Integer是非线程安全的；
 * （4）判断是否大于3次，大于的话抛出异常信息，否则进行密码校验，返回信息。
 */
public class MyHashedCredentialsMatcher extends HashedCredentialsMatcher {

    //String：useranem,AtomicInteger:输入次数.
    private Cache<String, AtomicInteger> passwordRetryCache;

    public MyHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    /**
     * 判断是否超过了3次.
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        //1/获取用户信息.
        String username = (String) token.getPrincipal();
        //2/获取到输入次数.
        AtomicInteger retryCount = passwordRetryCache.get(username);
        //3/判断输入次数是否为null，因为第一次的话，是不存在的。
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }

        //4/判断是否超过3次:incrementAndGet : ++,返回当前的数; 0-->incrementAndGet-->1
        if (retryCount.incrementAndGet() > 3) {
            throw new ExcessiveAttemptsException();
        }

        //5、进行密码校验.
        boolean matches = super.doCredentialsMatch(token, info);

        //6、校验通过的话，需要清空缓存。
        if (matches) {
            passwordRetryCache.remove(username);
        }

        return matches;
    }

}
