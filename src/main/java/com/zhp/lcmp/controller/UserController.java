package com.zhp.lcmp.controller;

import com.zhp.lcmp.service.IUserService;
import com.zhp.lcmp.vo.RestResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation("获得用户信息")
    @PostMapping("/getUserInfo")
    public RestResult getUserInfo(Integer userId) {

        return RestResult.fromData(userService.getById(userId));
    }

}
