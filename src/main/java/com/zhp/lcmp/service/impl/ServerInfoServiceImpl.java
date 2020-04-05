package com.zhp.lcmp.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.lcmp.dao.ServerInfoDao;
import com.zhp.lcmp.entity.ServerInfoEntity;
import com.zhp.lcmp.service.IServerInfoService;
import com.zhp.lcmp.util.RemoteShellExecutionUtil;
import com.zhp.lcmp.vo.DaskInfoVo;
import com.zhp.lcmp.vo.MemoryUsageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务器信息服务层实现
 *
 * @author ZhaoHP
 * @date 2020/2/16 15:51
 */
@Slf4j
@Service
public class ServerInfoServiceImpl extends ServiceImpl<ServerInfoDao,ServerInfoEntity> implements IServerInfoService {

    private static final String RE = "\\s+";

    @Autowired
    private ServerInfoDao serverInfoDao;

    @Override
    public Page<ServerInfoEntity> getServerInfoByUid(int userId, Integer pageNum, Integer pageSize) {
        Page<ServerInfoEntity> pagination = new Page<>(pageNum,pageSize);
        System.out.println(JSON.toJSONString(pagination));
        List<ServerInfoEntity> serverInfoList = this.baseMapper.getServerInfoList(pagination, userId);
        pagination.setRecords(serverInfoList);
        System.out.println(JSON.toJSONString(pagination));
        return pagination;
    }

    @Override
    public Map<String, Object> getServerUsageInfo(int serverId) {
        Map<String, Object> serverUsageInfoMap = new HashMap<>(16);
        serverUsageInfoMap.put("daskInfo", getDiskInfo(serverId));
        serverUsageInfoMap.put("memoryUsageInfo", getMemoryUsage(serverId));
        return serverUsageInfoMap;
    }


    public List<DaskInfoVo> getDiskInfo(int serverId){
        String exec = RemoteShellExecutionUtil.exec("df -h");
        log.info("服务器信息：\n"+exec);
        System.out.println(exec);
        String[] split = exec.split("\n");
        List<DaskInfoVo> daskInfoVoList = new ArrayList<>();
        for (String s : split) {
            String[] s1 = s.split(RE);
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
        String[] split1 = split[0].split(RE);
        MemoryUsageVo memoryUsageVo = new MemoryUsageVo();
        memoryUsageVo.setTotal(split1[1]);
        memoryUsageVo.setUsed(split1[2]);
        memoryUsageVo.setFree(split1[3]);
        memoryUsageVo.setShared(split1[4]);
        memoryUsageVo.setBuffCache(split1[5]);
        memoryUsageVo.setAvailable(split1[6]);
        return memoryUsageVo;
    }
}
