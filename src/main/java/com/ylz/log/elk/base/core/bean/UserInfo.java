package com.ylz.log.elk.base.core.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity //JPA
public class UserInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue//id自增长.
	private long uid;//用户id.
	
	private String username;//账号.
	
	private String name;//名称，昵称或者姓名.
	
	private String password;//密码.
	private String salt;//密码加密的盐. 主要加强密码的安全性.
	
	private byte state;//用户状态，0：创建用户；1：正常；2：用户被锁定了.
	
	/*
	 * 关联信息的配置... 用户--角色： 多对多的关系.
	 * --------------------
	 * 1个用户可以有多个角色.
	 * 1个角色可以被多个用户所拥有
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="SysUserRole",joinColumns={@JoinColumn(name="uid")},inverseJoinColumns={@JoinColumn(name="roleId")})
	private List<SysRole> roles;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}
	
	//为了密码的更安全. username+salt;
	public String getUserNameAndSalt() {
		return this.username+this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	

	public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
