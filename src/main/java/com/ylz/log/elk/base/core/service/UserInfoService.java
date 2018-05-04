package com.ylz.log.elk.base.core.service;

import com.ylz.log.elk.base.core.bean.UserInfo;

public interface UserInfoService {
	
	public UserInfo findByUsername(String name);
	
}
