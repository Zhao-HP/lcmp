package com.zhp.lcmp.controller;

import com.zhp.lcmp.entity.UserEntity;
import com.zhp.lcmp.service.IUserService;
import com.zhp.lcmp.vo.RestResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @ApiOperation("根据用户ID获得用户信息")
    @GetMapping("/getUserInfo")
    public RestResult getUserInfo(Integer userId) {
        return RestResult.fromData(userService.getUserInfo(userId));
    }


    @ApiOperation("根据用户名或邮箱获得用户信息")
    @ApiImplicitParam(name = "account", value = "账号")
    @PostMapping("/getUserInfoByNameOrMail")
    public RestResult getUserInfoByNameOrMail(String account) {
        return RestResult.fromData(userService.getUserInfoByNameOrMail(account));
    }


    @ApiOperation("用户注册")
    @PostMapping("/userRegister")
    public RestResult userRegister(@RequestBody UserEntity userEntity) {
        // String username,String password, String email
        return null;
    }
    //
    // @RequestMapping("/test")
    // public String test(){
    //     return "Hello World";
    // }
}
