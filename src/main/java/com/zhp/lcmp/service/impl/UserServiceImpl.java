package com.zhp.lcmp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.lcmp.dao.UserDao;
import com.zhp.lcmp.entity.UserEntity;
import com.zhp.lcmp.service.IUserService;
import com.zhp.lcmp.util.MailUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
    public UserEntity getUserInfoByNameOrMail(String account) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<UserEntity>();
        if (account.contains("@")) {
            wrapper.eq("email", account);
        } else {
            wrapper.eq("username", account);
        }
        return userDao.selectOne(wrapper);
    }

    @Override
    public int activationAccount(int userId, String identifyingCode) {
        UserEntity userInfo = getUserInfo(userId);
        if (StringUtils.isEmpty(identifyingCode) || !identifyingCode.equals(userInfo.getIdentifyingCode())) {
            return 0;
        }
        userInfo.setActivation(true);
        return userDao.updateById(userInfo);
    }

    @Override
    public int userRegister(UserEntity userEntity) {
        String randomString = RandomStringUtils.randomAlphanumeric(30);
        userEntity.setActivation(false);
        userEntity.setIdentifyingCode(randomString);
        int insert = userDao.insert(userEntity);
        sendActivationMail(userEntity.getUsername());
        return insert;
    }

    private void sendActivationMail(String username) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UserEntity userEntity = getUserInfoByNameOrMail(username);
                MailUtil.sendMail(userEntity.getEmail(), userEntity);
            }
        });
        thread.start();
    }

    @Override
    public UserEntity getUserInfo(Integer userId) {
        return userDao.selectById(userId);
    }

    @Override
    public int setUpdatePasswordCode(Integer userId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setPasswordCode(RandomStringUtils.randomNumeric(6));
        return this.userDao.updateById(userEntity);
    }
}
