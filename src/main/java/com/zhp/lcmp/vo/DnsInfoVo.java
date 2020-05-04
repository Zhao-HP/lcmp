package com.zhp.lcmp.vo;

import lombok.Data;

/**
 * 服务器上单条DNS的列表
 *
 * @author ZhaoHP
 * @date 2020/5/4 14:19
 */
@Data
public class DnsInfoVo {
    private Integer id;
    private String domain;
    private String ipAddress;
}
