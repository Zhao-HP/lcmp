package com.zhp.lcmp.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.lcmp.constant.Constant;
import com.zhp.lcmp.dao.ConfigFileInfoDao;
import com.zhp.lcmp.dao.ServerInfoDao;
import com.zhp.lcmp.dto.EasyConfigDto;
import com.zhp.lcmp.entity.ConfigFileInfoEntity;
import com.zhp.lcmp.entity.ServerInfoEntity;
import com.zhp.lcmp.service.IConfigFileInfoService;
import com.zhp.lcmp.util.FileUtil;
import com.zhp.lcmp.util.RemoteShellExecutionUtil;
import com.zhp.lcmp.vo.ConfigFileListVo;
import com.zhp.lcmp.vo.DnsInfoVo;
import com.zhp.lcmp.vo.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 配置文件信息服务实现层
 *
 * @author ZhaoHP
 * @date 2020/5/1 17:51
 */
@Slf4j
@Service
public class ConfigFileInfoServiceImpl extends ServiceImpl<ConfigFileInfoDao, ConfigFileInfoEntity> implements IConfigFileInfoService {

    @Autowired
    private ConfigFileInfoDao configFileInfoDao;

    @Autowired
    private ServerInfoDao serverInfoDao;

    @Override
    public List<ConfigFileInfoEntity> getConfigFileInfoListByUserIdAndServerId(Integer userId, Integer serverId) {

        QueryWrapper<ConfigFileInfoEntity> wrapper = new QueryWrapper<>();
        if (null != userId) {
            wrapper.eq("user_id", userId);
        }
        if (null != serverId) {
            wrapper.eq("server_id", serverId);
        }
        return configFileInfoDao.selectList(wrapper);
    }

    @Override
    public RestResult saveConfigFileInfo(ConfigFileInfoEntity configFileInfoEntity) {
        configFileInfoEntity.setCreateTime(new Date());
        configFileInfoEntity.setUpdateTime(new Date());
        ConfigFileInfoEntity result = selectFileInfoByUserIdAndCode(configFileInfoEntity.getUserId(), configFileInfoEntity.getConfigCode());
        if (null != result) {
            return RestResult.fromErrorMessage("保存失败，配置码已经存在，请重新输入配置码");
        } else {
            configFileInfoDao.insert(configFileInfoEntity);
            return RestResult.fromData("保存成功");
        }
    }

    @Override
    public ConfigFileInfoEntity selectFileInfoByUserIdAndCode(Integer userId, String configCode) {
        return configFileInfoDao.selectFileInfoByUserIdAndCode(userId, configCode);
    }

    @Override
    public int deleteConfigFileInfoById(Integer id) {
        return configFileInfoDao.deleteById(id);
    }

    @Override
    public RestResult updateConfigFileInfo(ConfigFileInfoEntity configFileInfoEntity) {
        configFileInfoEntity.setUpdateTime(new Date());
        int result = configFileInfoDao.updateById(configFileInfoEntity);
        if (result > 0) {
            return RestResult.fromData("更新成功");
        } else {
            return RestResult.fromErrorMessage("更新失败");
        }
    }

    @Override
    public Page<ConfigFileListVo> getConfigFileListByPage(int pageNum, int pageSize, Integer userId) {
        Page<ConfigFileListVo> page = new Page<>(pageNum, pageSize);
        page.setRecords(configFileInfoDao.getConfigFileListByPage(page, userId));
        return page;
    }

    @Override
    public String readConfigFileContent(String configCode, Integer userId, Integer serverId) {
        Map<String, String> baseInfoMap = getBaseInfo(configCode, userId, serverId);
        log.info("获得配置文件内容的基础信息：{}", JSON.toJSONString(baseInfoMap));
        FileUtil.createDirs(baseInfoMap.get(Constant.BASE_INFO_LOCAL_DIR_PATH));
        ServerInfoEntity serverInfoEntity = serverInfoDao.selectById(serverId);
        log.info("获得配置文件内容；服务器信息：{}", JSON.toJSONString(serverInfoEntity));
        boolean result = RemoteShellExecutionUtil.copyFile(baseInfoMap.get(Constant.BASE_INFO_REMOTE_FILE_PATH), baseInfoMap.get(Constant.BASE_INFO_LOCAL_DIR_PATH), serverInfoEntity);
        if (!result) {
            return null;
        }
        String fileContent = FileUtil.readFileContent(baseInfoMap.get(Constant.BASE_INFO_LOCAL_FILE_PATH));
        FileUtil.deleteFile(baseInfoMap.get(Constant.BASE_INFO_LOCAL_FILE_PATH));
        return fileContent;
    }

    @Override
    public void updateConfigFileContent(Integer userId, Integer serverId, String configCode, String fileContent) {
        Map<String, String> baseInfoMap = getBaseInfo(configCode, userId, serverId);
        log.info("更新配置文件内容的基础信息：{}", JSON.toJSONString(baseInfoMap));
        FileUtil.wirteFileContent(baseInfoMap.get(Constant.BASE_INFO_LOCAL_FILE_PATH), fileContent);
        ServerInfoEntity serverInfoEntity = serverInfoDao.selectById(serverId);
        log.info("更新配置文件内容；服务器信息：{}", JSON.toJSONString(serverInfoEntity));
        RemoteShellExecutionUtil.putFile(baseInfoMap.get(Constant.BASE_INFO_LOCAL_FILE_PATH), baseInfoMap.get(Constant.BASE_INFO_REMOTE_DIR_PATH), serverInfoEntity);
        FileUtil.deleteFile(baseInfoMap.get(Constant.BASE_INFO_LOCAL_FILE_PATH));
    }

    @Override
    public void updateDnsInfoListByServerId(Integer serverId, EasyConfigDto easyConfigDto) {
        List<DnsInfoVo> dnsList = easyConfigDto.getDnsList();
        StringBuffer buffer = new StringBuffer();
        for (DnsInfoVo dnsInfoVo : dnsList) {
            buffer.append(dnsInfoVo.getDomain()).append(" ").append(dnsInfoVo.getIpAddress()).append("\n");
        }
        updateConfigFileContent(null, serverId, easyConfigDto.getConfigCode(), buffer.toString());
    }

    /**
     * 获得配置文件相关的基础信息
     *
     * @param userId
     * @param configCode
     * @param serverId
     * @return
     */
    private Map<String, String> getBaseInfo(String configCode, Integer userId, Integer serverId) {
        ConfigFileInfoEntity configFileInfoEntity = configFileInfoDao.selectFileInfoByUserIdAndCode(userId, configCode);
        log.info("基本信息之配置文件信息：{}", JSON.toJSONString(configFileInfoEntity));
        ServerInfoEntity serverInfoEntity = serverInfoDao.selectById(serverId);
        log.info("基本信息之服务器信息：{}", JSON.toJSONString(serverInfoEntity));
        String localDirPath = Constant.TMP_DIR + (null == userId ? "common" : userId);
        String localFilePath = localDirPath + "/" + StringUtils.substringAfterLast(configFileInfoEntity.getConfigFilePath(), "/");
        String remoteDirPath = StringUtils.substringBeforeLast(configFileInfoEntity.getConfigFilePath(), "/");
        Map<String, String> baseInfoMap = new HashMap<>(16);
        baseInfoMap.put(Constant.BASE_INFO_IP_ADDRESS, serverInfoEntity.getIpAddress());
        baseInfoMap.put(Constant.BASE_INFO_USERNAME, serverInfoEntity.getLoginName());
        baseInfoMap.put(Constant.BASE_INFO_PASSWORD, serverInfoEntity.getLoginPwd());
        baseInfoMap.put(Constant.BASE_INFO_REMOTE_DIR_PATH, remoteDirPath);
        baseInfoMap.put(Constant.BASE_INFO_REMOTE_FILE_PATH, configFileInfoEntity.getConfigFilePath());
        baseInfoMap.put(Constant.BASE_INFO_LOCAL_DIR_PATH, localDirPath);
        baseInfoMap.put(Constant.BASE_INFO_LOCAL_FILE_PATH, localFilePath);
        return baseInfoMap;
    }

    @Override
    public List<DnsInfoVo> getDnsInfoListByServerId(String configCode, Integer serverId) {
        String fileContent = readConfigFileContent(configCode, null, serverId);
        String[] dnsArray = fileContent.split("\n");
        List<DnsInfoVo> dnsInfoVos = new ArrayList<>(16);
        for (String singleDns : dnsArray) {
            DnsInfoVo dnsInfoVo = new DnsInfoVo();
            dnsInfoVo.setId(dnsInfoVos.size() + 1);
            dnsInfoVo.setDomain(StringUtils.substringBefore(singleDns, " "));
            dnsInfoVo.setIpAddress(StringUtils.substringAfter(singleDns, " "));
            dnsInfoVos.add(dnsInfoVo);
        }
        return dnsInfoVos;
    }

}