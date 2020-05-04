package com.zhp.lcmp.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.lcmp.entity.ApplicationInfoEntity;
import com.zhp.lcmp.entity.ServerInfoEntity;
import com.zhp.lcmp.vo.RestResult;

import java.util.List;
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
     * @param ipAddress
     * @param info
     * @return
     */
    Page<ServerInfoEntity> getServerInfoByUid(int userId, Integer pageNum, Integer pageSize, String ipAddress, String info);

    /**
     * 返回服务器的使用情况
     *
     * @param serverId
     * @return
     */
    Map<String, Object> getServerUsageInfo(int serverId);

    /**
     * 保存或者更新服务器信息
     *
     * @param serverInfoEntity
     * @return
     */
    RestResult saveOrUpdateServerInfoById(ServerInfoEntity serverInfoEntity);

    /**
     * 获得应用列表
     *
     * @param pageNum
     * @param pageSize
     * @param status
     */
    Page<ApplicationInfoEntity> getApplicationListByStatus(int pageNum, int pageSize, String status);

    /**
     * 应用安装
     *
     * @param packageName
     * @return
     */
    RestResult installedApplication(String packageName);

    /**
     * 更新软件包
     *
     * @param packageName
     * @return
     */
    RestResult updateApplication(String packageName);

    /**
     * 移除软件包
     *
     * @param packageName
     * @return
     */
    RestResult removeApplication(String packageName);

    /**
     * 根据用户名和IP获得对应的服务器信息
     *
     * @param userId
     * @return
     */
    List<ServerInfoEntity> selectServerInfoListByUserId(Integer userId);

}
