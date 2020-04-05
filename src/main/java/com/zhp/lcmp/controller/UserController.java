package com.zhp.lcmp.controller;

import com.zhp.lcmp.entity.UserEntity;
import com.zhp.lcmp.service.IServerInfoService;
import com.zhp.lcmp.service.IUserService;
import com.zhp.lcmp.vo.RestResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户相关操作的控制层
 *
 * @author ZhaoHP
 * @date 2020/2/2 17:32
 */
@ApiModel("用户模块")
@RestController
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IServerInfoService serverInfoService;

    @ApiOperation("根据用户ID获得用户信息")
    @PostMapping("/getUserInfo")
    public RestResult getUserInfo(@RequestParam("userId") Integer userId) {
        return RestResult.fromData(userService.getUserInfo(userId));
    }


    @ApiOperation("根据用户名或邮箱获得用户信息")
    @ApiImplicitParam(name = "account", value = "账号")
    @PostMapping("/getUserInfoByNameOrMail")
    public RestResult getUserInfoByNameOrMail(@RequestParam("username") String username) {
        UserEntity userInfoByNameOrMail = userService.getUserInfoByNameOrMail(username);
        if (userInfoByNameOrMail != null){
            return RestResult.fromData(userInfoByNameOrMail);
        }else {
            return RestResult.fromErrorMessage("没有此用户，请确认用户名是否正确");
        }
    }


    @ApiOperation("用户注册")
    @PostMapping("/userRegister")
    public RestResult userRegister(@RequestBody UserEntity userEntity) {
        return null;
    }


    @ApiOperation("根据用户ID获得该用户的服务器信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "userId", value = "用户ID")
    )
    @PostMapping("/getServerInfoByUid")
    public RestResult getServerInfoByUid(@RequestParam("userId") int userId, @RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize) {
        return RestResult.fromData(serverInfoService.getServerInfoByUid(userId, pageNum, pageSize));
    }
}
