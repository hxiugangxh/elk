package com.ylz.log.elk.base.core.repository;


import com.ylz.log.elk.base.core.bean.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
	
	//通过用户名查找用户信息.
	public UserInfo findByUsername(String name);
}
