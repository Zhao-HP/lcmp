package com.zhp.lcmp.vo;

import lombok.Data;

/**
 * 内存使用情况
 *
 * @author ZhaoHP
 * @date 2020/4/4 15:54
 */
@Data
public class MemoryUsageVo {
    private String total;
    private String used;
    private String free;
    private String shared;
    private String buffCache;
    private String available;
}
