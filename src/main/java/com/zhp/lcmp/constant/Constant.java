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
    String BASE_INFO_LOCAL_FILE_PATH ="localFilePath";
    String BASE_INFO_LOCAL_DIR_PATH="localDirPath";
    String BASE_INFO_SERVER_INFO="serverInfo";
}
