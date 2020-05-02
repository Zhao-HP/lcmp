package com.zhp.lcmp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhp.lcmp.dao.ConfigFileInfoDao;
import com.zhp.lcmp.entity.ConfigFileInfoEntity;
import com.zhp.lcmp.service.IConfigFileInfoService;
import com.zhp.lcmp.service.IServerInfoService;
import com.zhp.lcmp.vo.ConfigFileListVo;
import com.zhp.lcmp.vo.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    private IServerInfoService serverInfoService;

    @Override
    public List<ConfigFileInfoEntity> getConfigFileInfoListByUserIdAndServerId(Integer userId, Integer serverId) {

        QueryWrapper<ConfigFileInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.isNull("user_id");
        wrapper.or();
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
        boolean result = checkConfigCodeIsExist(configFileInfoEntity.getUserId(), configFileInfoEntity.getConfigCode());
        if (result){
            return RestResult.fromErrorMessage("保存失败，配置码已经存在，请重新输入配置码");
        }else{
            configFileInfoDao.insert(configFileInfoEntity);
            return RestResult.fromData("保存成功");
        }
    }

    @Override
    public boolean checkConfigCodeIsExist(Integer userId, String configCode){
        QueryWrapper<ConfigFileInfoEntity> wrapper= new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("config_code",configCode);
        List<ConfigFileInfoEntity> configFileInfoEntities = baseMapper.selectList(wrapper);
        if (null == configFileInfoEntities || configFileInfoEntities.size() == 0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public RestResult updateConfigFileInfo(ConfigFileInfoEntity configFileInfoEntity) {
        configFileInfoEntity.setUpdateTime(new Date());
        int result = configFileInfoDao.updateById(configFileInfoEntity);
        if (result >0){
            return RestResult.fromData("更新成功");
        }else{
            return RestResult.fromErrorMessage("更新失败");
        }
    }

    @Override
    public Page<ConfigFileListVo> getConfigFileListByPage(int pageNum, int pageSize, Integer userId) {
        Page<ConfigFileListVo> page = new Page<>(pageNum, pageSize);
        page.setRecords(configFileInfoDao.getConfigFileListByPage(page, userId));
        return page;
    }

}