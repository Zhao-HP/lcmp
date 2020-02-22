package com.zhp.lcmp.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.lcmp.entity.ServerInfoEntity;

/**
 * 服务器信息服务接口层
 *
 * @author ZhaoHP
 * @date 2020/2/16 15:52
 */
public interface IServerInfoService extends IService<ServerInfoEntity> {

    /**
     * 根据userId获得服务器信息
     * @param userId
     * @return
     */
	Page<ServerInfoEntity> getServerInfoByUid(int userId, Integer pageNum, Integer pageSize);
}
