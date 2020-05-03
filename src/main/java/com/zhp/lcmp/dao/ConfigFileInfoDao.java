package com.zhp.lcmp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhp.lcmp.entity.ConfigFileInfoEntity;
import com.zhp.lcmp.vo.ConfigFileListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配置文件信息持久层
 *
 * @author ZhaoHP
 * @date 2020/5/1 17:42
 */
@Mapper
@Component
public interface ConfigFileInfoDao extends BaseMapper<ConfigFileInfoEntity> {

    /**
     * 分页获得配置文件列表
     * @param page
     * @param userId
     * @return
     */
    List<ConfigFileListVo> getConfigFileListByPage(Page page, @Param("userId") Integer userId);

    /**
     * 根据用户ID和配置码查询配置文件信息
     * @param userId
     * @param configCode
     * @return
     */
    ConfigFileInfoEntity selectFileInfoByUserIdAndCode(@Param("userId") Integer userId, @Param("configCode") String configCode);
}
