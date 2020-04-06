package com.zhp.lcmp.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhp.lcmp.entity.ServerInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务器信息持久层
 * 
 * @author ZhaoHP
 * @date 2020/2/16 15:50
 */
@Mapper
@Component
public interface ServerInfoDao extends BaseMapper<ServerInfoEntity> {

    /**
     * 分页获得服务器列表
     * @param pagination
     * @param userId
     * @param ipAddress
     * @param info
     * @return
     */
    List<ServerInfoEntity> getServerInfoList(@Param("page") Page<ServerInfoEntity> pagination,@Param("userId") int userId, @Param("ipAddress") String ipAddress, @Param("info") String info);

}
