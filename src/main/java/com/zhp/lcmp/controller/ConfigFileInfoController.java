package com.zhp.lcmp.controller;

import com.zhp.lcmp.entity.ConfigFileInfoEntity;
import com.zhp.lcmp.service.IConfigFileInfoService;
import com.zhp.lcmp.util.RequestUtil;
import com.zhp.lcmp.vo.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 配置文件信息控制层
 *
 * @author ZhaoHP
 * @date 2020/5/1 17:52
 */
@Slf4j
@RestController
@Api(value = "配置文件模块",tags = {"配置文件模块"})
public class ConfigFileInfoController {

    @Autowired
    private IConfigFileInfoService configFileInfoService;

    @ApiOperation("根据用户ID和服务器ID获得配置文件列表")
    @GetMapping("/getConfigFileInfoListByUserIdAndServerId")
    public RestResult getConfigFileInfoListByUserIdAndServerId(HttpServletRequest request){
        Integer userId = RequestUtil.getUserId(request);
        Integer serverId = RequestUtil.getServerId(request);
        return RestResult.fromData(configFileInfoService.getConfigFileInfoListByUserIdAndServerId(userId, serverId));
    }

    @ApiOperation("保存或更新配置文件信息")
    @PostMapping("/saveOrUpdateConfigFileInfo")
    public RestResult saveOrUpdateConfigFileInfo(ConfigFileInfoEntity configFileInfoEntity, HttpServletRequest request){
        Integer userId = RequestUtil.getUserId(request);
        configFileInfoEntity.setUserId(userId);
        if (null != userId){
            if (null == configFileInfoEntity.getId()){
                return configFileInfoService.saveConfigFileInfo(configFileInfoEntity);
            }else{
                return configFileInfoService.updateConfigFileInfo(configFileInfoEntity);
            }
        }else{
            return RestResult.fromErrorMessage("用户ID为空");
        }
    }

    @ApiOperation("分页获得用户的配置文件列表")
    @GetMapping("/getConfigFileListByPage")
    public  RestResult getConfigFileListByPage(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, HttpServletRequest request){
        Integer userId = RequestUtil.getUserId(request);
        return RestResult.fromData(configFileInfoService.getConfigFileListByPage(pageNum, pageSize, userId));
    }

    @ApiOperation("根据ID删除配置文件信息")
    @GetMapping("/deleteConfigFileInfoById")
    public RestResult deleteConfigFileInfoById(@RequestParam("id")Integer id, HttpServletRequest request){

        Integer userId = RequestUtil.getUserId(request);
        if (null != userId){
            int result = configFileInfoService.deleteConfigFileInfoById(id);
            System.out.println(result);
            if (result > 0){
                return RestResult.fromData("删除成功");
            }else{
                return RestResult.fromErrorMessage("删除失败");
            }
        }else{
            return RestResult.fromErrorMessage("用户ID为空");
        }

    }
}
