package com.nanomt88.demo.rocketmq.service.impl;


import com.nanomt88.demo.rocketmq.dao.UserDao;
import com.nanomt88.demo.rocketmq.entity.User;
import com.nanomt88.demo.rocketmq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 
 * @ClassName UserServiceImpl
 * @author abel
 * @date 2016年11月10日
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	/**
	 * 
	 * @param name
	 * @return
	 */
	public User getName(String name) {
		return userDao.findByName(name);
	}
}
