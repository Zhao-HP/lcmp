package com.zhp.lcmp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.lcmp.dao.UserDao;
import com.zhp.lcmp.entity.UserEntity;
import com.zhp.lcmp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户相关操作的服务层实现
 *
 * @author ZhaoHP
 * @date 2020/2/2 18:07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserEntity getUserInfoByNameOrMail(String usernameOrEmail) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<UserEntity>();
        if (usernameOrEmail.contains("@")) {
            wrapper.eq("email", usernameOrEmail);
        } else {
            wrapper.eq("username", usernameOrEmail);
        }

        return userDao.selectOne(wrapper);
    }

    @Override
    public UserEntity getUserInfo(Integer userId) {
        return userDao.selectById(userId);
    }
}
