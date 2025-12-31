package org.superdata.medismart.security.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.superdata.medismart.common.ResponseResult;
import org.superdata.medismart.common.constant.CacheConstants;
import org.superdata.medismart.common.exception.ServiceException;
import org.superdata.medismart.entity.SysUser;
import org.superdata.medismart.mapper.sql.SysUserMapper;
import org.superdata.medismart.security.domain.LoginUser;
import org.superdata.medismart.service.SysUserService;
import org.superdata.medismart.utils.security.JwtUtil;
import org.superdata.medismart.utils.database.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserService sysUserService;

    @Override
    public ResponseResult login(SysUser user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate;
        try{
            authenticate = authenticationManager.authenticate(authenticationToken);
            if (Objects.isNull(authenticate)) {
                throw new BadCredentialsException("用户名或密码错误");
            }
        } catch (BadCredentialsException bce){
            throw new ServiceException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject(CacheConstants.USER_LOGIN_KEY + userId, loginUser);
        //把token响应给前端
        HashMap<String, String> data = new HashMap<>();
        data.put("token", jwt);
        return new ResponseResult(200, "登陆成功", data);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject(CacheConstants.USER_LOGIN_KEY + userid);
        return new ResponseResult(200, "退出成功");
    }

    @Override
    public ResponseResult register(SysUser sysUser) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        sysUser.setPassword(encodedPassword);
        sysUserMapper.insertUser(sysUser);
        return null;
    }
}
