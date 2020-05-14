package com.zhp.lcmp.controller;

import com.zhp.lcmp.entity.UserEntity;
import com.zhp.lcmp.service.IServerInfoService;
import com.zhp.lcmp.service.IUserService;
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
 * 用户相关操作的控制层
 *
 * @author ZhaoHP
 * @date 2020/2/2 17:32
 */
@Slf4j
@RestController
@Api(value = "用户模块", tags = {"用户模块"})
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

    @ApiOperation("激活用户账号")
    @GetMapping("/activationAccount")
    public String activationAccount(@RequestParam("userId") int userId, @RequestParam("identifyingCode") String identifyingCode) {
        int result = userService.activationAccount(userId, identifyingCode);
        if (result > 0) {
            return "激活成功";
        } else {
            return "激活失败";
        }
    }

    @ApiOperation("根据用户名或邮箱获得用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    @PostMapping("/getUserInfoByNameOrMail")
    public RestResult getUserInfoByNameOrMail(@RequestParam("account") String account, @RequestParam("password") String password) {
        UserEntity userEntity = userService.getUserInfoByNameOrMail(account);
        if (!userEntity.isActivation()) {
            return RestResult.fromErrorMessage("当前用户未激活，请前往邮箱激活");
        } else if (!password.equals(userEntity.getPassword())) {
            return RestResult.fromErrorMessage("密码输入错误，请重新输入密码");
        }
        return RestResult.fromData(userEntity);

    }

    @ApiOperation("用户注册")
    @PostMapping("/userRegister")
    public RestResult userRegister(UserEntity userEntity) {
        UserEntity userInfoByUsername = userService.getUserInfoByNameOrMail(userEntity.getUsername());
        UserEntity userInfoByEmaiil = userService.getUserInfoByNameOrMail(userEntity.getEmail());
        if (null != userInfoByUsername) {
            return RestResult.fromErrorMessage("当前用户名已经存在，请重新输入");
        } else if (null != userInfoByEmaiil) {
            return RestResult.fromErrorMessage("当前邮箱已经和其他用户名绑定，请重新输入");
        }
        int result = userService.userRegister(userEntity);
        if (result > 0) {
            return RestResult.fromData("用户注册成功，请前往邮箱激活");
        } else {
            return RestResult.fromErrorMessage("注册失败");
        }
    }

    @ApiOperation("设置修改密码用得的验证码")
    @PostMapping("/setUpdatePasswordCode")
    public RestResult setUpdatePasswordCode(HttpServletRequest request) {
        Integer userId = RequestUtil.getUserId(request);
        if (null == userId) {
            int result = userService.setUpdatePasswordCode(userId);
            if (result > 0) {
                return RestResult.fromData("验证码已发送至您的邮箱，请查看");
            } else {
                return RestResult.fromErrorMessage("验证码发送失败，请重新发送");
            }
        } else {
            return RestResult.fromErrorMessage("请求失败");
        }
    }

}
