package com.ylz.log.elk.base.core.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController {
	
	@RequestMapping("/userAdd")
	@RequiresPermissions("userInfo:add")
	public String userAdd(){
		return "/userInfoAdd";
	}
	
	
	@RequestMapping("/userDel")
	@RequiresPermissions("userInfo:del")
	public String userDel(){
		return "/userInfoDel";
	}
}
