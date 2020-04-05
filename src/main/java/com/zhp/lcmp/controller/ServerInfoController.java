package com.zhp.lcmp.controller;

import com.zhp.lcmp.service.IServerInfoService;
import com.zhp.lcmp.vo.RestResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器信息控制层
 *
 * @author ZhaoHP
 * @date 2020/2/16 15:53
 */
@Slf4j
@RestController
public class ServerInfoController {

    @Autowired
    private IServerInfoService serverInfoService;

    /**
     * 获得服务器上各个目录的使用情况
     *
     * @return
     */
    @ApiOperation("获得服务器使用情况")
    @PostMapping("/getServerUsageInfo")
    public RestResult getServerUsageInfo(@RequestParam("serverId") int serverId) {
        log.info("服务器ID：" + serverId);
        return RestResult.fromData(serverInfoService.getServerUsageInfo(serverId));
    }

}
