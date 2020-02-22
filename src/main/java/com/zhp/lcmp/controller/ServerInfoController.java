package com.zhp.lcmp.controller;

import com.zhp.lcmp.service.IServerInfoService;
import com.zhp.lcmp.vo.RestResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@RestController
public class ServerInfoController {

    @Autowired
    private IServerInfoService serverInfoService;

    @ApiOperation("根据用户ID获得该用户的服务器信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "userId", value = "用户ID")
    )
    @PostMapping("/getServerInfoByUid")
    public RestResult getServerInfoByUid(@RequestParam("userId") int userId, @RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize) {
        return RestResult.fromData(serverInfoService.getServerInfoByUid(userId, pageNum,pageSize));
    }

}
