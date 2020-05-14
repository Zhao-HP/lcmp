package com.zhp.lcmp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhp.lcmp.dto.EasyConfigDto;
import com.zhp.lcmp.entity.ConfigFileInfoEntity;
import com.zhp.lcmp.vo.ConfigFileListVo;
import com.zhp.lcmp.vo.DnsInfoVo;
import com.zhp.lcmp.vo.IfcfgEth0;
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
    public ConfigFileInfoEntity selectFileInfoByUserIdAndCode(Integer userId, String configCode);

    /**
     * 根据ID删除配置文件信息
     *
     * @param id
     * @return
     */
    int deleteConfigFileInfoById(Integer id);

    /**
     * 获得服务器上的DNS列表
     *
     * @param configCode
     * @param serverId
     * @return
     */
    List<DnsInfoVo> getDnsInfoListByServerId(String configCode, Integer serverId);

    /**
     * 获得ifcfg-eth0的内容
     * @param configCode
     * @param serverId
     * @return
     */
    IfcfgEth0 getIfcfgEth0Content(String configCode, Integer serverId);

    /**
     * 根据配置码获得配置文件内容
     *
     * @param configCode
     * @param userId
     * @param serverId
     * @return
     */
    String readConfigFileContent(String configCode, Integer userId, Integer serverId);

    /**
     * 更新配置文件内容
     *
     * @param fileContent
     * @param userId
     * @param serverId
     * @param configCode
     */
    void updateConfigFileContent(Integer userId, Integer serverId, String configCode, String fileContent);

    /**
     * 简易配置修改文件内容
     *
     * @param serverId
     * @param easyConfigDto
     */
    void updateDnsInfoListByServerId(Integer serverId, EasyConfigDto easyConfigDto);
}
