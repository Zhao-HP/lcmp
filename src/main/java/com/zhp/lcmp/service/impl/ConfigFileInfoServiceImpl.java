package com.zhp.lcmp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.zhp.lcmp.vo.IfcfgEth0;
import com.zhp.lcmp.vo.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

    private static RestTemplate template = new RestTemplate();

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
        Map<String, String> baseInfo = getBaseInfo(configCode, userId, serverId);
        log.info("获得配置文件内容的基础信息：{}", JSON.toJSONString(baseInfo));
        FileUtil.createDirs(baseInfo.get(Constant.BASE_INFO_LOCAL_DIR_PATH));
        ServerInfoEntity serverInfoEntity = serverInfoDao.selectById(serverId);
        log.info("获得配置文件内容；服务器信息：{}", JSON.toJSONString(serverInfoEntity));
        boolean result = RemoteShellExecutionUtil.copyFile(baseInfo.get(Constant.BASE_INFO_REMOTE_FILE_PATH), baseInfo.get(Constant.BASE_INFO_LOCAL_DIR_PATH), serverInfoEntity);
        if (!result) {
            return null;
        }
        String fileContent = FileUtil.readFileContent(baseInfo.get(Constant.BASE_INFO_LOCAL_FILE_PATH));
        FileUtil.deleteFile(baseInfo.get(Constant.BASE_INFO_LOCAL_FILE_PATH));
        return fileContent;
    }

    @Override
    public void updateConfigFileContent(Integer userId, Integer serverId, String configCode, String fileContent) {
        if (StringUtils.isNotEmpty(fileContent)) {
            Map<String, String> baseInfo = getBaseInfo(configCode, userId, serverId);
            log.info("更新配置文件内容的基础信息：{}", JSON.toJSONString(baseInfo));
            FileUtil.wirteFileContent(baseInfo.get(Constant.BASE_INFO_LOCAL_FILE_PATH), fileContent);
            ServerInfoEntity serverInfoEntity = serverInfoDao.selectById(serverId);
            log.info("更新配置文件内容；服务器信息：{}", JSON.toJSONString(serverInfoEntity));
            RemoteShellExecutionUtil.putFile(baseInfo.get(Constant.BASE_INFO_LOCAL_FILE_PATH), baseInfo.get(Constant.BASE_INFO_REMOTE_DIR_PATH), serverInfoEntity);
            FileUtil.deleteFile(baseInfo.get(Constant.BASE_INFO_LOCAL_FILE_PATH));
        }
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
        ConfigFileInfoEntity configFileInfo = configFileInfoDao.selectFileInfoByUserIdAndCode(userId, configCode);
        log.info("基本信息之配置文件信息：{}", JSON.toJSONString(configFileInfo));
        ServerInfoEntity serverInfo = serverInfoDao.selectById(serverId);
        log.info("基本信息之服务器信息：{}", JSON.toJSONString(serverInfo));
        String localDirPath = Constant.TMP_DIR + (null == userId ? "common" : userId);
        String localFilePath = localDirPath + "/" + StringUtils.substringAfterLast(configFileInfo.getConfigFilePath(), "/");
        String remoteDirPath = StringUtils.substringBeforeLast(configFileInfo.getConfigFilePath(), "/");
        Map<String, String> baseInfoMap = new HashMap<>(16);
        baseInfoMap.put(Constant.BASE_INFO_IP_ADDRESS, serverInfo.getIpAddress());
        baseInfoMap.put(Constant.BASE_INFO_USERNAME, serverInfo.getLoginName());
        baseInfoMap.put(Constant.BASE_INFO_PASSWORD, serverInfo.getLoginPwd());
        baseInfoMap.put(Constant.BASE_INFO_REMOTE_DIR_PATH, remoteDirPath);
        baseInfoMap.put(Constant.BASE_INFO_REMOTE_FILE_PATH, configFileInfo.getConfigFilePath());
        baseInfoMap.put(Constant.BASE_INFO_LOCAL_DIR_PATH, localDirPath);
        baseInfoMap.put(Constant.BASE_INFO_LOCAL_FILE_PATH, localFilePath);
        return baseInfoMap;
    }

    @Override
    public void updateDnsInfoListByServerId(Integer serverId, EasyConfigDto easyConfigDto) {
        String fileContent = null;
        if (Constant.EASY_CONFIG_CONFIG_CODE_DNS.equals(easyConfigDto.getConfigCode())) {
            fileContent = getDnsString(easyConfigDto.getDnsList());
        } else if (Constant.EASY_CONFIG_CONFIG_CODE_IFCFG_ETH0.equals(easyConfigDto.getConfigCode())) {
            fileContent = getEthString(easyConfigDto.getEthInfo());
        }

        updateConfigFileContent(null, serverId, easyConfigDto.getConfigCode(), fileContent);
    }

    /**
     * 拼接Dns的简易配置文件内容
     * @param dnsInfoVoList
     * @return
     */
    private String getDnsString(List<DnsInfoVo> dnsInfoVoList) {
        StringBuffer buffer = new StringBuffer();
        for (DnsInfoVo dnsInfoVo : dnsInfoVoList) {
            buffer.append(dnsInfoVo.getDomain()).append(" ").append(dnsInfoVo.getIpAddress()).append("\n");
        }
        return buffer.toString();
    }

    /**
     * 拼接网卡的简易配置文件内容
     * @param ifcfgEth0
     * @return
     */
    private String getEthString(IfcfgEth0 ifcfgEth0) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("TYPE=Ethernet").append("\n");
        if (StringUtils.isNotEmpty(ifcfgEth0.getDevice())) {
            buffer.append("DEVICE=").append(ifcfgEth0.getDevice()).append("\n");
        }
        if (StringUtils.isNotEmpty(ifcfgEth0.getOnboot())) {
            buffer.append("ONBOOT=").append(ifcfgEth0.getOnboot()).append("\n");
        }
        if (StringUtils.isNotEmpty(ifcfgEth0.getBootproto())) {
            buffer.append("BOOTPROTO=").append(ifcfgEth0.getBootproto()).append("\n");
        }
        if (StringUtils.isNotEmpty(ifcfgEth0.getHwaddr())) {
            buffer.append("HWADDR=").append(ifcfgEth0.getHwaddr()).append("\n");
        }
        if (StringUtils.isNotEmpty(ifcfgEth0.getIpaddr())) {
            buffer.append("IPADDR=").append(ifcfgEth0.getIpaddr()).append("\n");
        }
        if (StringUtils.isNotEmpty(ifcfgEth0.getNetmask())) {
            buffer.append("NETMASK=").append(ifcfgEth0.getNetmask()).append("\n");
        }
        if (StringUtils.isNotEmpty(ifcfgEth0.getGateway())) {
            buffer.append("GATEWAY=").append(ifcfgEth0.getGateway()).append("\n");
        }
        if (StringUtils.isNotEmpty(ifcfgEth0.getDns1())) {
            buffer.append("DNS1=").append(ifcfgEth0.getDns1()).append("\n");
        }
        if (StringUtils.isNotEmpty(ifcfgEth0.getDns2())) {
            buffer.append("DNS2=").append(ifcfgEth0.getDns2()).append("\n");
        }
        return buffer.toString();
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

    @Override
    public IfcfgEth0 getIfcfgEth0Content(String configCode, Integer serverId) {
        String fileContent = readConfigFileContent(configCode, null, serverId);
        String[] splitList = fileContent.split("\n");
        IfcfgEth0 ifcfgEth0 = new IfcfgEth0();
        for (String splits : splitList) {
            String[] split = splits.split("=");
            if (Constant.IFCFG_ETH0_PARAM_DEVICE.equals(split[0])){
                ifcfgEth0.setDevice(split[1]);
            }else if (Constant.IFCFG_ETH0_PARAM_ONBOOT.equals(split[0])){
                ifcfgEth0.setOnboot(split[1]);
            }else if (Constant.IFCFG_ETH0_PARAM_BOOTPROTO.equals(split[0])){
                ifcfgEth0.setBootproto(split[1]);
            }else if (Constant.IFCFG_ETH0_PARAM_HWADDR.equals(split[0])){
                ifcfgEth0.setHwaddr(split[1]);
            }else if (Constant.IFCFG_ETH0_PARAM_IPADDR.equals(split[0])){
                ifcfgEth0.setIpaddr(split[1]);
            }else if (Constant.IFCFG_ETH0_PARAM_NETMASK.equals(split[0])){
                ifcfgEth0.setNetmask(split[1]);
            }else if (Constant.IFCFG_ETH0_PARAM_GATEWAY.equals(split[0])){
                ifcfgEth0.setGateway(split[1]);
            }else if (Constant.IFCFG_ETH0_PARAM_DNS1.equals(split[0])){
                ifcfgEth0.setDns1(split[1]);
            }else if (Constant.IFCFG_ETH0_PARAM_DNS2.equals(split[0])){
                ifcfgEth0.setDns2(split[1]);
            }
        }
        return ifcfgEth0;
    }

    public static void main(String[] args) {
        Set<String> deptSet = getDeptSet("102303", new HashSet<>());
        Set<String> userCodeSet = new HashSet<>(16);
        deptSet.forEach((k) -> {
            userCodeSet.addAll(getUserListByDeptCode(k, userCodeSet));
        });
        Set<String> userImgUrlSet = new HashSet<>(16);
        userCodeSet.forEach((K) -> {
            String userImgUrl = getUserImgUrlSet(K);
            if (StringUtils.isNotEmpty(userImgUrl)) {
                userImgUrlSet.add(userImgUrl);
            }
        });
        System.out.println(userImgUrlSet);
    }

    public static Set<String> getDeptSet(String deptCode, Set<String> deptSet) {
        String deptApi = "http://ehrnew.ziroom.com/api/ehr/getChildOrgs.action?parentId={parentId}&setId=101";
        JSONObject deptObject = template.getForObject(deptApi, JSONObject.class, deptCode);
        if ("0".equals(deptObject.getString("errorCode"))) {
            JSONArray data = deptObject.getJSONArray("data");
            for (int i = 0; i < data.size(); i++) {
                String code = data.getJSONObject(i).getString("code");
                getDeptSet(code, deptSet);
            }
        } else {
            deptSet.add(deptCode);
        }

        return deptSet;
    }

    private static Set<String> getUserListByDeptCode(String deptCode, Set<String> userCodeSet) {
        log.info("deptCode：{}", deptCode);
        String userApi = "http://ehrnew.ziroom.com/api/ehr/getUsers.action?deptId={deptId}&setId=101";
        JSONObject forObject = template.getForObject(userApi, JSONObject.class, deptCode);
        System.out.println(forObject.getString("errorCode"));
        if ("101".equals(forObject.getString("errorCode"))) {
            return userCodeSet;
        } else {
            JSONArray data = forObject.getJSONArray("data");
            for (int i = 0; i < data.size(); i++) {
                String username = data.getJSONObject(i).getString("username");
                userCodeSet.add(username);
            }
            return userCodeSet;
        }
    }

    private static String getUserImgUrlSet(String userCode) {
        String userImgUrl = "http://ehrstatic.ziroom.com/" + userCode + ".jpg";
        try {
            ResponseEntity<JSONObject> forEntity = template.getForEntity(userImgUrl, JSONObject.class, userCode);
        } catch (HttpClientErrorException e) {
            log.info("照片不存在");
            return null;
        } catch (RestClientException e) {
            log.info("文件存在");
        }
        return userImgUrl;
    }

}