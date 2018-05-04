package com.ylz.log.elk.base.core.service.impl;

import com.ylz.log.elk.base.core.bean.UserInfo;
import com.ylz.log.elk.base.core.repository.UserInfoRepository;
import com.ylz.log.elk.base.core.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Override
	public UserInfo findByUsername(String name) {
		return userInfoRepository.findByUsername(name);
	}

}
