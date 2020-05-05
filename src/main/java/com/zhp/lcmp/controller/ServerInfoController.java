package com.zhp.lcmp.controller;

import com.zhp.lcmp.entity.ServerInfoEntity;
import com.zhp.lcmp.service.IServerInfoService;
import com.zhp.lcmp.util.RequestUtil;
import com.zhp.lcmp.vo.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务器信息控制层
 *
 * @author ZhaoHP
 * @date 2020/2/16 15:53
 */
@Slf4j
@RestController
@Api(value = "服务器模块", tags = {"服务器模块"})
public class ServerInfoController {

    @Autowired
    private IServerInfoService serverInfoService;

    @ApiOperation("获得服务器使用情况")
    @PostMapping("/getServerUsageInfo")
    public RestResult getServerUsageInfo(HttpServletRequest request) {
        Integer serverId = RequestUtil.getServerId(request);
        if (null != serverId) {
            return RestResult.fromData(serverInfoService.getServerUsageInfo(serverId));
        } else {
            return RestResult.fromErrorMessage("请求失败");
        }
    }

    @ApiOperation("根据用户ID分页获得该用户的服务器信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID"),
            @ApiImplicitParam(name = "pageNum", value = "当前页数"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示的记录数"),
            @ApiImplicitParam(name = "ipAddress", value = "IP地址"),
            @ApiImplicitParam(name = "info", value = "服务器的描述信息")
    })
    @PostMapping("/getServerInfoByUid")
    public RestResult getServerInfoByUid(@RequestParam("userId") int userId, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam(name = "ipAddress", required = false) String ipAddress, @RequestParam(name = "info", required = false) String info) {
        return RestResult.fromData(serverInfoService.getServerInfoByUid(userId, pageNum, pageSize, ipAddress, info));
    }

    @ApiOperation("保存或者更新服务器信息")
    @PostMapping("/saveOrUpdateServerInfoById")
    public RestResult saveOrUpdateServerInfoById(ServerInfoEntity serverInfoEntity, HttpServletRequest request) {
        Integer userId = RequestUtil.getUserId(request);
        if (null != userId) {
            return serverInfoService.saveOrUpdateServerInfoById(serverInfoEntity);
        } else {
            return RestResult.fromErrorMessage("请求失败");
        }
    }

    @ApiOperation("获得应用列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页数"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示的记录数"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String")
    })
    @PostMapping("/getApplicationList")
    public RestResult getApplicationList(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize,
                                         @RequestParam("status") String status, HttpServletRequest request) {
        Integer serverId = RequestUtil.getServerId(request);
        if (null != serverId) {
            return RestResult.fromData(serverInfoService.getApplicationListByStatus(pageNum, pageSize, status, serverId));
        } else {
            return RestResult.fromErrorMessage("请求失败");
        }
    }

    @ApiOperation("安装软件包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageName", value = "包名", dataType = "String")
    })
    @PostMapping("/installedApplication")
    public RestResult installedApplication(@RequestParam("packageName") String packageName, HttpServletRequest request) {
        Integer serverId = RequestUtil.getServerId(request);
        if (null != serverId) {
            return serverInfoService.installedApplication(packageName, serverId);
        } else {
            return RestResult.fromErrorMessage("请求失败");
        }
    }

    @ApiOperation("更新软件包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageName", value = "包名", dataType = "String")
    })
    @PostMapping("/updateApplication")
    public RestResult updateApplication(@RequestParam("packageName") String packageName, HttpServletRequest request) {
        Integer serverId = RequestUtil.getServerId(request);
        if (null != serverId) {
            return serverInfoService.updateApplication(packageName, serverId);
        } else {
            return RestResult.fromErrorMessage("请求失败");
        }
    }

    @ApiOperation("移除软件包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageName", value = "包名", dataType = "String")
    })
    @PostMapping("/removeApplication")
    public RestResult removeApplication(@RequestParam("packageName") String packageName, HttpServletRequest request) {
        Integer serverId = RequestUtil.getServerId(request);
        if (null != serverId) {
            return serverInfoService.removeApplication(packageName, serverId);
        } else {
            return RestResult.fromErrorMessage("请求失败");
        }
    }

    @ApiOperation("根据用户名，获得服务器列表")
    @GetMapping("/getServerListByUserId")
    public RestResult getServerListByUserId(HttpServletRequest request) {
        Integer userId = RequestUtil.getUserId(request);
        return RestResult.fromData(serverInfoService.selectServerInfoListByUserId(userId));
    }

}
