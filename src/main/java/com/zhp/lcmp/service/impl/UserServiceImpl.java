package com.zhp.lcmp.service.impl;

import com.zhp.lcmp.dao.UserDao;
import com.zhp.lcmp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

}
