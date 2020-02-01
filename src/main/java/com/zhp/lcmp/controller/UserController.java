package com.zhp.lcmp.controller;

import com.zhp.lcmp.service.IUserService;
import com.zhp.lcmp.vo.RestResult;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation("测试接口")
    @RequestMapping("/test")
    public RestResult test(){
        log.info("测试接口运行");
        return RestResult.fromData("hello world");
    }

}
