package com.zhp.lcmp.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 从请求头获取信息的相关工具类
 *
 * @author ZhaoHP
 * @date 2020/5/1 17:46
 */
public class RequestUtil {

    public static Integer getUserId(HttpServletRequest request){
        String userId = request.getHeader("userId");
        if (!StringUtils.isEmpty(userId)){
            return Integer.valueOf(userId);
        }
        return null;
    }

    public static Integer getServerId(HttpServletRequest request){
        String serverId = request.getHeader("serverId");
        if (!StringUtils.isEmpty(serverId)){
            return Integer.valueOf(serverId);
        }
        return null;
    }

}
