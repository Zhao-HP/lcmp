package com.zhp.lcmp.controller;

import com.alibaba.fastjson.JSON;
import com.zhp.lcmp.dto.EasyConfigDto;
import com.zhp.lcmp.entity.ConfigFileInfoEntity;
import com.zhp.lcmp.service.IConfigFileInfoService;
import com.zhp.lcmp.service.IServerInfoService;
import com.zhp.lcmp.util.RequestUtil;
import com.zhp.lcmp.vo.DnsInfoVo;
import com.zhp.lcmp.vo.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 配置文件信息控制层
 *
 * @author ZhaoHP
 * @date 2020/5/1 17:52
 */
@Slf4j
@RestController
@Api(value = "配置文件模块", tags = {"配置文件模块"})
public class ConfigFileInfoController {

    @Autowired
    private IConfigFileInfoService configFileInfoService;
    @Autowired
    private IServerInfoService serverInfoService;

    @ApiOperation("根据用户ID和服务器ID获得配置文件列表")
    @GetMapping("/getConfigFileInfoListByUserIdAndServerId")
    public RestResult getConfigFileInfoListByUserIdAndServerId(HttpServletRequest request) {
        Integer userId = RequestUtil.getUserId(request);
        Integer serverId = RequestUtil.getServerId(request);
        return RestResult.fromData(configFileInfoService.getConfigFileInfoListByUserIdAndServerId(userId, serverId));
    }

    @ApiOperation("保存或更新配置文件信息")
    @PostMapping("/saveOrUpdateConfigFileInfo")
    public RestResult saveOrUpdateConfigFileInfo(ConfigFileInfoEntity configFileInfoEntity, HttpServletRequest request) {
        Integer userId = RequestUtil.getUserId(request);
        configFileInfoEntity.setUserId(userId);
        if (null != userId) {
            if (null == configFileInfoEntity.getId()) {
                return configFileInfoService.saveConfigFileInfo(configFileInfoEntity);
            } else {
                return configFileInfoService.updateConfigFileInfo(configFileInfoEntity);
            }
        } else {
            return RestResult.fromErrorMessage("用户ID为空");
        }
    }

    @ApiOperation("分页获得用户的配置文件列表")
    @GetMapping("/getConfigFileListByPage")
    public RestResult getConfigFileListByPage(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, HttpServletRequest request) {
        Integer userId = RequestUtil.getUserId(request);
        return RestResult.fromData(configFileInfoService.getConfigFileListByPage(pageNum, pageSize, userId));
    }

    @ApiOperation("根据ID删除配置文件信息")
    @GetMapping("/deleteConfigFileInfoById")
    public RestResult deleteConfigFileInfoById(@RequestParam("id") Integer id, HttpServletRequest request) {

        Integer userId = RequestUtil.getUserId(request);
        if (null != userId) {
            int result = configFileInfoService.deleteConfigFileInfoById(id);
            System.out.println(result);
            if (result > 0) {
                return RestResult.fromData("删除成功");
            } else {
                return RestResult.fromErrorMessage("删除失败");
            }
        } else {
            return RestResult.fromErrorMessage("用户ID为空");
        }

    }

    @ApiOperation("更新配置文件的内容")
    @PostMapping("/updateConfigFileContent")
    public RestResult updateConfigFileContent(String configCode, String fileContent, HttpServletRequest request) {
        Integer userId = RequestUtil.getUserId(request);
        Integer serverId = RequestUtil.getServerId(request);
        if (null != userId && null != serverId) {
            configFileInfoService.updateConfigFileContent(userId, serverId, configCode, fileContent);
            return RestResult.fromData("更新成功");
        } else {
            return RestResult.fromErrorMessage("请求失败");
        }
    }

    @ApiOperation("获取配置文件的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configCode", value = "配置码", dataType = "String")
    })
    @GetMapping("/getConfigFileContent")
    public RestResult getConfigFileContent(@RequestParam("configCode") String configCode, HttpServletRequest request) {
        Integer userId = RequestUtil.getUserId(request);
        Integer serverId = RequestUtil.getServerId(request);
        if (null != userId) {
            String configFileContent = configFileInfoService.readConfigFileContent(configCode, userId, serverId);
            log.info("配置文件内容：{}", configFileContent);
            if (StringUtils.isNotEmpty(configFileContent)) {
                return RestResult.fromData(configFileContent);
            } else {
                return RestResult.fromErrorMessage("获得配置文件失败，请检查文件是否存在");
            }
        } else {
            return RestResult.fromErrorMessage("用户ID为空");
        }
    }

    @ApiOperation("获得服务器上的DNS列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configCode", value = "配置码", dataType = "String")
    })
    @GetMapping("/getDnsInfoListByServerId")
    public RestResult getDnsInfoListByServerId(String configCode, HttpServletRequest request) {
        Integer serverId = RequestUtil.getServerId(request);
        if (null != serverId) {
            List<DnsInfoVo> dnsInfoVoList = configFileInfoService.getDnsInfoListByServerId(configCode, serverId);
            return RestResult.fromData(dnsInfoVoList);
        } else {
            return RestResult.fromErrorMessage("请求失败");
        }
    }

    @ApiOperation(("更新服务器上的DNS配置文件"))
    @PostMapping(value = "/updateDnsInfoListByServerId", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResult updateDnsInfoListByServerId(HttpServletRequest request) {
        Integer serverId = RequestUtil.getServerId(request);
        if (null != serverId){
            String[] parameterValues = request.getParameterValues("0");
            EasyConfigDto easyConfigDto = JSON.parseObject(parameterValues[0], EasyConfigDto.class);
            configFileInfoService.updateDnsInfoListByServerId(serverId, easyConfigDto);
            return RestResult.fromData("更新成功");
        }else {
            return RestResult.fromErrorMessage("请求失败");
        }
    }

}

