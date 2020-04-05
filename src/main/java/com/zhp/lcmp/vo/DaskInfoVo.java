package com.zhp.lcmp.vo;

import lombok.Data;

/**
 * 服务器磁盘信息
 *
 * @author ZhaoHP
 * @date 2020/4/4 11:14
 */
@Data
public class DaskInfoVo {
    private String fileSystem;
    private String size;
    private String used;
    private String avail;
    private String use;
    private String mountedOn;
}
