package org.superdata.medismart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.CollectionUtils;
import org.superdata.medismart.entity.SysRole;
import org.superdata.medismart.entity.SysUser;
import org.superdata.medismart.mapper.sql.SysRoleMapper;
import org.superdata.medismart.service.SysUserService;
import org.superdata.medismart.mapper.sql.SysUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 针对表【sys_user(用户表)】的数据库操作Service实现
 * @createDate 2024-04-15 21:06:09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private SysUserMapper userMapper;

    @Resource
    private SysRoleMapper roleMapper;

    @Override
    public String selectUserRoleGroup(Long id) {
        List<SysRole> list = roleMapper.selectRolesByUserId(id);
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        return list.stream().map(SysRole::getName).collect(Collectors.joining(","));
    }

    @Override
    public Boolean resetUserPwd(String userName, String newPassword) {
        return userMapper.updateUserPassword(userName, newPassword);
    }

    @Override
    public Boolean updateUserAvatar(String username, String avatar) {
        return userMapper.updateUserAvatar(username, avatar);
    }

    @Override
    public Boolean checkPhoneUnique(SysUser user) {
        if (user.getPhonenumber() == null) {
            return true;
        }
        SysUser sysUser = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhonenumber, user.getPhonenumber()));
        return sysUser == null || (user.getId() != null && user.getId().equals(sysUser.getId()));
    }

    @Override
    public Boolean checkEmailUnique(SysUser user) {
        if (user.getEmail() == null) {
            return true;
        }
        SysUser sysUser = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, user.getEmail()));
        return sysUser == null || (user.getId() != null && user.getId().equals(sysUser.getId()));
    }
}




