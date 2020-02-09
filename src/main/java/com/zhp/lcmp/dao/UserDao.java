package com.zhp.lcmp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhp.lcmp.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 用户相关操作的持久层接口
 *
 * @author ZhaoHP
 * @ClassName UserDao
 * @date 2020/1/23 11:41
 */
@Component
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
}
