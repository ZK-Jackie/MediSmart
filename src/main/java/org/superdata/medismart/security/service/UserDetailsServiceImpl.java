package org.superdata.medismart.security.service;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.superdata.medismart.common.exception.ServiceException;
import org.superdata.medismart.entity.SysUser;
import org.superdata.medismart.mapper.sql.SysMenuMapper;
import org.superdata.medismart.mapper.sql.SysUserMapper;
import org.superdata.medismart.security.domain.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserName,username);
        SysUser user = sysUserMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new ServiceException("用户名或密码错误");
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中
//        List<String> list = new ArrayList<>(Arrays.asList("test"));
        List<String> list = sysMenuMapper.selectPermsByUserId(user.getId());
        log.info("用户权限信息：{}",list);
        //封装成UserDetails对象返回
        LoginUser userDetail = new LoginUser(user, list);
        return userDetail;
    }
}


