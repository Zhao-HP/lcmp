package com.zhp.lcmp.vo;

import lombok.Data;

@Data
public class IfcfgEth0 {

    private String device;
    private String onboot;
    private String bootproto;
    private String hwaddr;
    private String ipaddr;
    private String netmask;
    private String gateway;
    private String dns1;
    private String dns2;

}
