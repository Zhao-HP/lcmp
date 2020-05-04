package com.zhp.lcmp.dto;

import com.zhp.lcmp.vo.DnsInfoVo;
import lombok.Data;

import java.util.List;

/**
 * 简易配置文件按钮的DNS接收实体类
 *
 * @author ZhaoHP
 * @date 2020/5/4 17:31
 */
@Data
public class EasyConfigDto {

    private List<DnsInfoVo>dnsList;
    private String configCode;
}
