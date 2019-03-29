package com.jxufe.tmall.service;

import com.jxufe.tmall.pojo.User;

public interface UserService extends BaseService{
	
	boolean isExist(String name);
	User get(String name,String password);
}
