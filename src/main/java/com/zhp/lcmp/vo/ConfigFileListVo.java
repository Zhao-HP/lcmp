package com.zhp.lcmp.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 配置文件列表视图层实体类
 *
 * @author ZhaoHP
 * @date 2020/5/1 20:20
 */
@Data
public class ConfigFileListVo {
    private String ipAddress;
    private Integer id;
    private String configDesc;
    private String configCode;
    private String info;
    private String configFilePath;
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
