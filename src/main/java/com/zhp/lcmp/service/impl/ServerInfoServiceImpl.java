package com.zhp.lcmp.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.lcmp.dao.ServerInfoDao;
import com.zhp.lcmp.entity.ServerInfoEntity;
import com.zhp.lcmp.service.IServerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务器信息服务层实现
 *
 * @author ZhaoHP
 * @date 2020/2/16 15:51
 */
@Service
public class ServerInfoServiceImpl extends ServiceImpl<ServerInfoDao,ServerInfoEntity> implements IServerInfoService {

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
}
