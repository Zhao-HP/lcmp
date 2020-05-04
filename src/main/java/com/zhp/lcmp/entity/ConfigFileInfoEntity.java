package com.zhp.lcmp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 配置文件信息实体类
 *
 * @author ZhaoHP
 * @date 2020/5/1 17:39
 */
@Data
@TableName("config_file_info")
public class ConfigFileInfoEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 配置码
     */
    private String configCode;
    /**
     * 配置描述
     */
    private String configDesc;
    /**
     * 配置文件路径
     */
    private String configFilePath;
    /**
     * 服务器ID
     */
    private Integer serverId;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 简易按钮是否可用
     */
    private Boolean easyBtnIsDisable;
    /**
     * 配置文件按钮是否可用
     */
    private boolean fileBtnIsDisable;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private java.util.Date updateTime;

}
