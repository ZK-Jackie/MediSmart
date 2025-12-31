package org.superdata.medismart.security.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.superdata.medismart.common.constant.CacheConstants;
import org.superdata.medismart.common.exception.ServiceException;
import org.superdata.medismart.entity.SysUser;
import org.superdata.medismart.service.SysUserService;
import org.superdata.medismart.utils.security.JwtUtil;
import org.superdata.medismart.utils.database.RedisCache;
import org.superdata.medismart.utils.WebUtils;
import io.jsonwebtoken.Claims;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Resource
    private RedisCache redisCache;

    @Resource
    private SysUserService sysUserService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String token = request.getHeader("token");
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("token非法");
        }
        // 获取用户信息
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getId, userid);
        SysUser loginUser = sysUserService.getOne(sysUserLambdaQueryWrapper);
        if (loginUser == null) {
            throw new ServiceException("用户未登录");
        }
        // 删除用户缓存记录
        redisCache.deleteObject(CacheConstants.USER_LOGIN_KEY + userid);

        WebUtils.renderString(response, "退出成功");
    }
}
