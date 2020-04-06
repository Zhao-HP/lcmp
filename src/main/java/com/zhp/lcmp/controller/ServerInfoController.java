package com.zhp.lcmp.controller;

import com.alibaba.fastjson.JSON;
import com.zhp.lcmp.entity.ServerInfoEntity;
import com.zhp.lcmp.service.IServerInfoService;
import com.zhp.lcmp.vo.RestResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @ApiOperation("根据用户ID获得该用户的服务器信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "userId", value = "用户ID")
    )
    @PostMapping("/getServerInfoByUid")
    public RestResult getServerInfoByUid(@RequestParam("userId") int userId, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam(name = "ipAddress", required = false) String ipAddress, @RequestParam(name = "info", required = false) String info) {
        return RestResult.fromData(serverInfoService.getServerInfoByUid(userId, pageNum, pageSize, ipAddress, info));
    }

    @ApiOperation("保存或者更新服务器信息")
    @PostMapping("/saveOrUpdateServerInfoById")
    public RestResult saveOrUpdateServerInfoById(ServerInfoEntity serverInfoEntity) {
        System.out.println(JSON.toJSONString(serverInfoEntity));
        return serverInfoService.saveOrUpdateServerInfoById(serverInfoEntity);
    }

    @ApiOperation("获得应用列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String")
    })
    @PostMapping("/getApplicationList")
    public RestResult getApplicationList(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("status") String status){
        return RestResult.fromData(serverInfoService.getApplicationListByStatus(pageNum, pageSize, status));
    }

    @ApiOperation("安装软件包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageName", value = "包名", dataType = "String")
    })
    @PostMapping("/installedApplication")
    public RestResult installedApplication(@RequestParam("packageName") String packageName){
        return serverInfoService.installedApplication(packageName);
    }

    @ApiOperation("更新软件包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageName", value = "包名", dataType = "String")
    })
    @PostMapping("/updateApplication")
    public RestResult updateApplication(@RequestParam("packageName") String packageName){
        return serverInfoService.updateApplication(packageName);
    }

    @ApiOperation("移除软件包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageName", value = "包名", dataType = "String")
    })
    @PostMapping("/removeApplication")
    public RestResult removeApplication(@RequestParam("packageName") String packageName){
        return serverInfoService.removeApplication(packageName);
    }
}
