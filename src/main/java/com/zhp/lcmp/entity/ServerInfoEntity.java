package com.zhp.lcmp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * 服务器信息实体类
 *
 * @author ZhaoHP
 * @date 2020/2/16 15:44
 */
@Data
@TableName("server_info")
public class ServerInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 登录服务器的用户名
     */
    private String loginName;

    /**
     * 登录服务器的密码
     */
    private String loginPwd;

    /**
     * 注释
     */
    private String info;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private java.util.Date updateTime;

    /**
     * 所属用户的ID号
     */
    private Integer userId;

    /**
     * 内存大小
     */
    private Integer ramNum;

    /**
     * CPU数
     */
    private Integer cpuNum;
}
