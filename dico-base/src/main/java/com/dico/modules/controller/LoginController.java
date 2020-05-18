package com.dico.modules.controller;

import com.alibaba.fastjson.JSON;
import com.dico.common.shiro.AuthenticationRequest;
import com.dico.common.utils.TokenHelper;
import com.dico.modules.domain.SysUser;
import com.dico.modules.domain.UserTokenState;
import com.dico.modules.service.SysUserService;
import com.dico.result.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录处理类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: LoginController
 * 创建时间: 2018/12/11
 */
@Slf4j
@RestController
@RequestMapping("auth")
@Api(tags = "登录模块", produces = "登录模块Api")
public class LoginController {

    @Autowired
    private TokenHelper tokenHelper;
    @Autowired
    private SysUserService sysUserService;

    @ResponseBody
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录的方法")
    public Object login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest request, HttpServletResponse response) {
        log.error("Request Json:" + JSON.toJSONString(authenticationRequest));
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        Subject subject = SecurityUtils.getSubject();
        ResultData resultData = new ResultData();
        try {
            if (!subject.isAuthenticated()) {
                subject = (new Subject.Builder()).buildSubject();
                ThreadContext.bind(subject);
            }
            // 该方法会调用JwtRealm中的doGetAuthenticationInfo方法
            subject.login(usernamePasswordToken);
            resultData.setCode(0).setMsg("登录成功");
        } catch (UnknownAccountException exception) {
            resultData.setCode(1).setMsg("帐号不存在");
        } catch (IncorrectCredentialsException exception) {
            resultData.setCode(2).setMsg("错误的凭证，用户名或密码不正确");
        } catch (LockedAccountException exception) {
            resultData.setCode(3).setMsg("账户已锁定");
        } catch (ExcessiveAttemptsException exception) {
            resultData.setCode(4).setMsg("错误次数过多");
        } catch (AuthenticationException exception) {
            resultData.setCode(5).setMsg("认证失败");
        }

        // 认证通过
        if (subject.isAuthenticated()) {
            SysUser sysUser = sysUserService.getUserByUsername(authenticationRequest.getUsername());
            String token = tokenHelper.generateToken(sysUser.getId(), DeviceUtils.getCurrentDevice(request));
            /**
             * 根据登录的设备类型设置token过期时间
             */
            int expiresIn;
            Device device = DeviceUtils.getCurrentDevice(request);
            if (device.isMobile() || device.isTablet()) {
                expiresIn = tokenHelper.getMobileExpiresIn();
                Map dataMap = new HashMap(4);
                dataMap.put("name", sysUser.getName());
                dataMap.put("icon", sysUser.getImgHref());
                dataMap.put("token", token);
                resultData.setData(dataMap);
            } else {
                expiresIn = tokenHelper.getExpiredIn();
                resultData.setData(new UserTokenState(token, expiresIn));
            }
            return resultData;
        } else {
            return resultData;
        }
    }
}
