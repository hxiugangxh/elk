package com.ylz.log.elk.base.shiro;

import com.google.code.kaptcha.Constants;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 自定义过滤器，在此过滤器中进行验证码的校验.
 *
 */
public class CustomFormAuthticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        if (isLoginSubmission(request, response)) {//是登录请求提交.
            String received = request.getParameter("kaptchaValidate");//获取前端输入的参数
            if (received != null) {
                //从sessoin获取进行校验.
                HttpServletRequest request2 = (HttpServletRequest) request;
                String captcha = (String) request2.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
                boolean b = received.equalsIgnoreCase(captcha);
                if (!b) {
                    request.setAttribute("shiroLoginFailure", "kaptchaValidateFailed");
                    return true;
                }
            }
        }
        return super.onAccessDenied(request, response);
    }

}
