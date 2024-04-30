package org.superdata.medismart.controller;

import lombok.extern.slf4j.Slf4j;
import org.superdata.medismart.common.ResponseResult;
import org.superdata.medismart.constant.CacheConstants;
import org.superdata.medismart.entity.SysUser;
import org.superdata.medismart.entity.request.UserLoginRequest;
import org.superdata.medismart.security.service.LoginService;
import org.superdata.medismart.utils.RedisCache;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class UserController {

    @Resource
    private LoginService loginService;

    @Resource
    private RedisCache redisCache;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserLoginRequest user){
        if (user == null){
            return new ResponseResult(401, "参数不能为空");
        }
        // 验证码校验
        validateCaptcha(user.getUsername(), user.getCode(), user.getUuid());
        // 登录前校验
        loginPreCheck(user.getUsername(), user.getPassword());
        SysUser sysUser = new SysUser();
        sysUser.setUserName(user.getUsername());
        sysUser.setPassword(user.getPassword());
        return loginService.login(sysUser);
    }



    public void validateCaptcha(String username, String code, String uuid){
        // 验证码校验
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        log.info("验证码key:{}", verifyKey);
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null){
            throw new RuntimeException("验证码已失效");
        }
        if (!code.equalsIgnoreCase(captcha)){
            throw new RuntimeException("验证码错误");
        }
    }

    public void loginPreCheck(String username, String password){
        if(username == null || password == null){
            throw new RuntimeException("用户名或密码不能为空");
        }
        if (password.length() < 6 || password.length() > 20){
            throw new RuntimeException("密码长度不符合要求");
        }
        if (username.length() < 6 || username.length() > 20){
            throw new RuntimeException("用户名长度不符合要求");
        }
    }

}
