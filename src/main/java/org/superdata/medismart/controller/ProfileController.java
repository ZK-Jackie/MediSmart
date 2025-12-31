package org.superdata.medismart.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.superdata.medismart.common.ResponseResult;
import org.superdata.medismart.common.constant.CacheConstants;
import org.superdata.medismart.entity.SysUser;
import org.superdata.medismart.security.domain.LoginUser;
import org.superdata.medismart.service.SysUserService;
import org.superdata.medismart.utils.database.RedisCache;
import org.superdata.medismart.utils.file.FileUtils;
import org.superdata.medismart.utils.file.MimeTypeUtils;
import org.superdata.medismart.utils.security.SecurityUtils;

import javax.annotation.Resource;

import java.util.HashMap;

import static org.superdata.medismart.utils.security.SecurityUtils.getLoginUser;

@Slf4j
@RestController
public class ProfileController {
    @Resource
    private SysUserService userService;

    @Resource
    private RedisCache redisCache;

    @GetMapping("/user/profile")
    public ResponseResult getProfile() {
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        HashMap<String, Object> responseResult = new HashMap<>();
        responseResult.put("user", user);
        responseResult.put("roleGroup", userService.selectUserRoleGroup(user.getId()));
        return ResponseResult.success(responseResult);
    }

    @PutMapping("/user/profile")
    public ResponseResult updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = getLoginUser();
        Long userId = loginUser.getUser().getId();
        SysUser currentUser = loginUser.getUser();
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());

        user.setId(userId);
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(currentUser)) {
            return ResponseResult.error("修改用户'" + loginUser.getUsername() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser)) {
            return ResponseResult.error("修改用户'" + loginUser.getUsername() + "'失败，邮箱账号已存在");
        }
        if (userService.updateById(user)) {
            // 更新缓存用户信息
            redisCache.setCacheObject(CacheConstants.USER_LOGIN_KEY + userId, loginUser);
            return ResponseResult.success(null);
        }
        return ResponseResult.error("修改个人信息异常，请联系管理员");
    }

    @PutMapping("/user/password")
    public ResponseResult updatePassword(String oldPassword, String newPassword) {
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST.value(), "修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST.value(), "新密码不能与旧密码相同");
        }
        newPassword = SecurityUtils.encryptPassword(newPassword);
        userService.resetUserPwd(userName, newPassword);
        return ResponseResult.success(null);
    }

    @PostMapping("/avatar")
    public ResponseResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUtils.upload("avatar", file, MimeTypeUtils.IMAGE_EXTENSION);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                //tokenService.setLoginUser(loginUser);
                //return data;
            }
        }
        return ResponseResult.error("上传图片异常，请联系管理员");
    }

}
