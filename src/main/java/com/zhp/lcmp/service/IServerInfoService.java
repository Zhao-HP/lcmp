package com.zhp.lcmp.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.lcmp.entity.ServerInfoEntity;

import java.util.Map;

/**
 * 用户服务器信息服务接口层
 *
 * @author ZhaoHP
 * @date 2020/2/16 15:52
 */
public interface IServerInfoService extends IService<ServerInfoEntity> {

    /**
     * 查询用户所有的服务器信息
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<ServerInfoEntity> getServerInfoByUid(int userId, Integer pageNum, Integer pageSize);

    /**
     * 返回服务器的使用情况
     *
     * @param serverId
     * @return
     */
    Map<String, Object> getServerUsageInfo(int serverId);

}
