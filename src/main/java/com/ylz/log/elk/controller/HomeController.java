
package com.ylz.log.elk.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.ylz.log.elk.base.core.bean.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private Producer producer;


    @RequestMapping(value = "/login")
    public String login(Map<String, Object> map) {

        return "index";
    }

    @RequestMapping(value = {"/", "/index"})
    public String index(Map<String, Object> map) {
        //获取到用户信息.Shiro.---SecurityUtils.getSubject() 获取到.UserInfo.
        Subject subject = SecurityUtils.getSubject();
        UserInfo userInfo = (UserInfo) subject.getPrincipal();

        System.out.println(userInfo.getRoles().get(0).getDescription());

        map.put("userInfo", userInfo);
        return "/index";
    }

    //只能使用get方式进行请求. post:在点击登录的时候进行使用.
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() {
        return "/login";
    }


    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public String login(HttpServletRequest request, Map<String, Object> map, PrincipalCollection principals) {

        //登录失败从request对象中可以获取shiro处理异常的细信息.
        //shiroLoginFailure.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        System.out.println("exception=" + exception);
        String msg = "";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                msg = "账号不存在";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                msg = "密码不正确";
            } else if (ExcessiveAttemptsException.class.getName().equals(exception)) {
                msg = "密码输入错误次数过多";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                msg = "验证码错误";
            } else {
                msg = "else -->" + exception;
            }
        }
        UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();

        System.out.println(userInfo);
        map.put("userInfo", userInfo);

        map.put("msg", msg);

        return "/login";
    }


    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse rsp) {

        //serlvet的知识.
        rsp.setDateHeader("Expires", 0);//失效时间.
        rsp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");//缓存配置.
        rsp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        rsp.setHeader("Pragma", "no-cache");
        rsp.setContentType("image/jpeg");//设置返回类型为图片.jpg

        HttpSession session = request.getSession();

        String captcha = producer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, captcha);
        ServletOutputStream out = null;
        try {
            out = rsp.getOutputStream();
            BufferedImage image = producer.createImage(captcha);
            ImageIO.write(image, "jpg", out);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

