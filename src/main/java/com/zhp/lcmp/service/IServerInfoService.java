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
     * @param userId
     * @return
     */
    List<ServerInfoEntity> selectServerInfoListByUserId(Integer userId);

    /**
     * 根据配置码获得配置文件内容
     * @param configCode
     * @param userId
     * @param serverId
     * @return
     */
    String getConfigFileContent(String configCode, Integer userId, Integer serverId);

    /**
     * 更新配置文件内容
     * @param fileContent
     * @param userId
     * @param serverId
     * @param configCode
     */
    void updateConfigFileContent(Integer userId,Integer serverId, String configCode, String fileContent);

}
