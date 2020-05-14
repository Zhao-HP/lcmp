package com.zhp.lcmp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author ZhaoHP
 * @date 2020/2/1 20:21
 */
@Data
@TableName("user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 是否激活
     */
    private boolean isActivation;

    /**
     * 随机产生的验证码
     */
    private String identifyingCode;

    /**
     * 修改密码的验证码
     */
    private String passwordCode;
}
