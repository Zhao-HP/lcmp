package com.zhp.lcmp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.lcmp.entity.ConfigFileInfoEntity;
import com.zhp.lcmp.vo.ConfigFileListVo;
import com.zhp.lcmp.vo.RestResult;

import java.util.List;

/**
 * 配置文件信息服务接口层
 *
 * @author ZhaoHP
 * @date 2020/5/1 17:46
 */
public interface IConfigFileInfoService extends IService<ConfigFileInfoEntity> {

    /**
     * 根据用户ID和服务器ID，获得配置文件列表
     *
     * @param userId
     * @param serverId
     * @return
     */
    public List<ConfigFileInfoEntity> getConfigFileInfoListByUserIdAndServerId(Integer userId, Integer serverId);

    /**
     * 添加配置文件信息
     *
     * @param configFileInfoEntity
     * @return
     */
    public RestResult saveConfigFileInfo(ConfigFileInfoEntity configFileInfoEntity);

    /**
     * 修改配置文件信息
     *
     * @param configFileInfoEntity
     * @return
     */
    public RestResult updateConfigFileInfo(ConfigFileInfoEntity configFileInfoEntity);

    /**
     * 分页获取用户的配置文件列表
     *
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    Page<ConfigFileListVo> getConfigFileListByPage(int pageNum, int pageSize, Integer userId);

    /**
     * 校验配置码是否存在
     *
     * @param userId
     * @param configCode
     * @return
     */
    public boolean checkConfigCodeIsExist(Integer userId, String configCode);

    /**
     * 根据ID删除配置文件信息
     * @param id
     * @return
     */
    int deleteConfigFileInfoById(Integer id);
}
