package com.ylz.log.elk.base.util;

//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.subject.Subject;

import java.security.Principal;

public class LoginInfoUtil {

    public static String getUserName() {
        String userName = "admin";
//        Subject subject = SecurityUtils.getSubject();
//        if (null != subject) {
//            Principal principal = (Principal) subject.getPrincipal();
//
//            userName = principal.getName();
//        }

        return userName;
    }
}
