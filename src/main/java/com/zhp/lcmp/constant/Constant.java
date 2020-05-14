package com.zhp.lcmp.constant;

/**
 * 常量类
 *
 * @author ZhaoHP
 * @date 2020/4/6 14:59
 */
public interface Constant {

    String APPLICATION_STATUS_ALL = "all";
    String APPLICATION_STATUS_UPDATED = "update";
    String APPLICATION_STATUS_INSTALLED = "installed";

    String YUM_LIST = "yum list";
    String YUM_LIST_INSTALLED = "yum list installed";
    String YUM_LIST_CHECK_UPDATE = "yum check-update";

    String TMP_DIR = "E:\\Code\\JAVA\\tmp\\";

    String BASE_INFO_IP_ADDRESS = "ipAddress";
    String BASE_INFO_USERNAME = "username";
    String BASE_INFO_PASSWORD = "password";
    String BASE_INFO_REMOTE_DIR_PATH = "remoteDirPath";
    String BASE_INFO_REMOTE_FILE_PATH = "remoteFilePath";
    String BASE_INFO_LOCAL_FILE_PATH = "localFilePath";
    String BASE_INFO_LOCAL_DIR_PATH = "localDirPath";
    String BASE_INFO_SERVER_INFO = "serverInfo";

    String EASY_CONFIG_CONFIG_CODE_DNS = "DNS";
    String EASY_CONFIG_CONFIG_CODE_IFCFG_ETH0 = "ifcfg-eth0";

    String IFCFG_ETH0_PARAM_DEVICE = "DEVICE";
    String IFCFG_ETH0_PARAM_ONBOOT = "ONBOOT";
    String IFCFG_ETH0_PARAM_BOOTPROTO = "BOOTPROTO";
    String IFCFG_ETH0_PARAM_HWADDR = "HWADDR";
    String IFCFG_ETH0_PARAM_IPADDR = "IPADDR";
    String IFCFG_ETH0_PARAM_NETMASK = "NETMASK";
    String IFCFG_ETH0_PARAM_GATEWAY = "GATEWAY";
    String IFCFG_ETH0_PARAM_DNS1 = "DNS1";
    String IFCFG_ETH0_PARAM_DNS2 = "DNS2";

}
