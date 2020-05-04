package com.zhp.lcmp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.lcmp.constant.Constant;
import com.zhp.lcmp.dao.ConfigFileInfoDao;
import com.zhp.lcmp.dao.ServerInfoDao;
import com.zhp.lcmp.entity.ApplicationInfoEntity;
import com.zhp.lcmp.entity.ServerInfoEntity;
import com.zhp.lcmp.service.IServerInfoService;
import com.zhp.lcmp.util.RemoteShellExecutionUtil;
import com.zhp.lcmp.vo.DaskInfoVo;
import com.zhp.lcmp.vo.MemoryUsageVo;
import com.zhp.lcmp.vo.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 服务器信息服务层实现
 *
 * @author ZhaoHP
 * @date 2020/2/16 15:51
 */
@Slf4j
@Service
public class ServerInfoServiceImpl extends ServiceImpl<ServerInfoDao, ServerInfoEntity> implements IServerInfoService {

    private static final String RE = "\\s+";

    @Autowired
    private ServerInfoDao serverInfoDao;
    @Autowired
    private ConfigFileInfoDao configFileInfoDao;

    @Override
    public Page<ServerInfoEntity> getServerInfoByUid(int userId, Integer pageNum, Integer pageSize, String ipAddress, String info) {
        Page<ServerInfoEntity> pagination = new Page<>(pageNum, pageSize);
        List<ServerInfoEntity> serverInfoList = this.baseMapper.getServerInfoList(pagination, userId, ipAddress, info);
        pagination.setRecords(serverInfoList);
        return pagination;
    }

    @Override
    public Map<String, Object> getServerUsageInfo(int serverId) {
        Map<String, Object> serverUsageInfoMap = new HashMap<>(16);
        serverUsageInfoMap.put("daskInfo", getDiskInfo(serverId));
        serverUsageInfoMap.put("memoryUsageInfo", getMemoryUsage(serverId));
        return serverUsageInfoMap;
    }

    @Override
    public RestResult saveOrUpdateServerInfoById(ServerInfoEntity serverInfoEntity) {
        if (serverInfoEntity.getId() != null) {
            return updateServerInfoById(serverInfoEntity);
        } else {
            return saveServerInfo(serverInfoEntity);
        }
    }

    @Override
    public Page<ApplicationInfoEntity> getApplicationListByStatus(int pageNum, int pageSize, String status) {
        Page<ApplicationInfoEntity> page = new Page<>(pageNum, pageSize);
        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize;
        int forStart = 2;
        String cmd = Constant.YUM_LIST;
        if (Constant.APPLICATION_STATUS_INSTALLED.equals(status)) {
            cmd = Constant.YUM_LIST_INSTALLED;
        } else if (Constant.APPLICATION_STATUS_UPDATED.equals(status)) {
            cmd = Constant.YUM_LIST_CHECK_UPDATE;
            forStart = 1;
        }
        List<ApplicationInfoEntity> yumList = getApplicationList(cmd, forStart);
        List<ApplicationInfoEntity> result = new ArrayList<>();
        page.setTotal(yumList.size());
        for (int i = start; i < end && i < yumList.size(); i++) {
            result.add(yumList.get(i));
        }
        page.setRecords(result);
        return page;
    }

    @Override
    public RestResult installedApplication(String packageName) {
        ApplicationInfoEntity applicationInfoEntity = existApplicationInstalled(packageName);
        if (applicationInfoEntity != null) {
            return RestResult.fromErrorMessage("应用已经安装，请勿重新安装");
        } else {
            String cmd = "yum -y install " + packageName;
            boolean result = execYumCmd(cmd);
            if (result) {
                return RestResult.fromData("安装成功");
            } else {
                return RestResult.fromErrorMessage("安装失败");
            }
        }
    }

    @Override
    public RestResult updateApplication(String packageName) {
        ApplicationInfoEntity applicationInfoEntity = existApplicationInstalled(packageName);
        if (applicationInfoEntity == null) {
            return RestResult.fromErrorMessage("应用未安装，请选择安装");
        } else {
            String cmd = "yum -y update " + packageName;
            boolean result = execYumCmd(cmd);
            System.out.println(result);
            if (result) {
                return RestResult.fromData("更新成功");
            } else {
                return RestResult.fromErrorMessage("更新失败");
            }
        }
    }

    @Override
    public RestResult removeApplication(String packageName) {
        ApplicationInfoEntity applicationInfoEntity = existApplicationInstalled(packageName);
        if (applicationInfoEntity == null) {
            return RestResult.fromErrorMessage("应用未安装，请选择安装");
        } else {
            String cmd = "yum -y remove " + packageName;
            boolean result = execYumCmd(cmd);
            if (result) {
                return RestResult.fromData("移除成功");
            } else {
                return RestResult.fromErrorMessage("移除失败");
            }
        }
    }

    @Override
    public List<ServerInfoEntity> selectServerInfoListByUserId(Integer userId) {
        QueryWrapper<ServerInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return serverInfoDao.selectList(wrapper);
    }

    private boolean execYumCmd(String cmd) {
        log.info("更新、安装、保存命令的执行命令：\n" + cmd);
        String exec = RemoteShellExecutionUtil.exec(cmd);
        log.info("更新、安装、保存命令的执行结果：\n" + exec);
        if (exec.contains("Complete!")) {
            return true;
        }
        return false;
    }

    /**
     * 保存服务器信息
     *
     * @param serverInfoEntity
     * @return
     */
    public RestResult saveServerInfo(ServerInfoEntity serverInfoEntity) {
        serverInfoEntity.setCreateTime(new Date());
        serverInfoEntity.setUpdateTime(new Date());
        try {
            this.baseMapper.insert(serverInfoEntity);
            return RestResult.fromData("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResult.fromErrorMessage("保存失败");
        }

    }

    /**
     * 根据ID更新服务器信息
     *
     * @param serverInfoEntity
     * @return
     */
    public RestResult updateServerInfoById(ServerInfoEntity serverInfoEntity) {
        serverInfoEntity.setUpdateTime(new Date());
        try {
            int i = this.baseMapper.updateById(serverInfoEntity);
            return RestResult.fromData("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResult.fromErrorMessage("更新失败");
        }
    }

    public List<DaskInfoVo> getDiskInfo(int serverId) {
        String exec = RemoteShellExecutionUtil.exec("df -h");
        log.info("服务器信息：\n" + exec);
        String[] split = exec.split("\n");
        List<DaskInfoVo> daskInfoVoList = new ArrayList<>();
        for (int i = 1; i < split.length; i++) {
            String[] s1 = split[i].split(RE);
            DaskInfoVo daskInfoVo = new DaskInfoVo();
            daskInfoVo.setFileSystem(s1[0]);
            daskInfoVo.setSize(s1[1]);
            daskInfoVo.setUsed(s1[2]);
            daskInfoVo.setAvail(s1[3]);
            daskInfoVo.setUse(s1[4]);
            daskInfoVo.setMountedOn(s1[5]);
            daskInfoVoList.add(daskInfoVo);
        }
        return daskInfoVoList;
    }


    public MemoryUsageVo getMemoryUsage(int serverId) {
        String exec = RemoteShellExecutionUtil.exec("free -m");
        log.info("内存使用情况：\n" + exec);
        String[] split = exec.split("\n");
        String[] split1 = split[1].split(RE);
        MemoryUsageVo memoryUsageVo = new MemoryUsageVo();
        memoryUsageVo.setTotal(split1[1]);
        memoryUsageVo.setUsed(split1[2]);
        memoryUsageVo.setFree(split1[3]);
        memoryUsageVo.setShared(split1[4]);
        memoryUsageVo.setBuffCache(split1[5]);
        memoryUsageVo.setAvailable(split1[6]);
        return memoryUsageVo;
    }

    public List<ApplicationInfoEntity> getApplicationList(String cmd, int start) {
        String exec = RemoteShellExecutionUtil.exec(cmd);
        String[] split = exec.split(RE);
        List<ApplicationInfoEntity> applicationInfoEntityList = new ArrayList<>();
        for (int i = start; i < split.length; i += 3) {
            if (i + 3 > split.length) {
                break;
            }
            ApplicationInfoEntity applicationInfoEntity = new ApplicationInfoEntity();
            applicationInfoEntity.setApplicationName(split[i]);
            applicationInfoEntity.setVersion(split[i + 1]);
            applicationInfoEntity.setOther(split[i + 2]);
            applicationInfoEntityList.add(applicationInfoEntity);
        }
        return applicationInfoEntityList;
    }

    public ApplicationInfoEntity existApplicationInstalled(String packageName) {
        String exec = RemoteShellExecutionUtil.exec("yum list installed | grep " + packageName);
        System.out.println("exec = " + exec);
        System.out.println(exec.length());
        System.out.println(exec == null);
        String[] split = exec.split(RE);
        if (exec == null || split.length < 1 || exec.length() == 0) {
            return null;
        }
        System.out.println("split = " + split);
        ApplicationInfoEntity applicationInfoEntity = new ApplicationInfoEntity();
        applicationInfoEntity.setApplicationName(split[0]);
        applicationInfoEntity.setVersion(split[1]);
        applicationInfoEntity.setOther(split[2]);
        return applicationInfoEntity;
    }

}
