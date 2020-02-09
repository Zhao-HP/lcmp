package com.zhp.lcmp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.lcmp.entity.UserEntity;

/**
 * 用户相关操作的服务层接口
 *
 * @author ZhaoHP
 * @date 2020/2/2 18:07
 */
public interface IUserService extends IService<UserEntity> {

    /**
     * 根据用户ID获得用户信息
     * @param userId
     * @return
     */
    UserEntity getUserInfo(Integer userId);


    /**
     * 根据用户名或者邮箱获得用户信息
     * @param usernameOrEmail
     * @return
     */
    UserEntity getUserInfoByNameOrMail(String usernameOrEmail);

}
