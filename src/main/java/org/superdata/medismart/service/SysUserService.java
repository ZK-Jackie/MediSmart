package org.superdata.medismart.service;

import org.superdata.medismart.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 23918
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2024-04-15 21:06:09
*/
public interface SysUserService extends IService<SysUser> {

    String selectUserRoleGroup(Long id);

    Boolean resetUserPwd(String userName, String newPassword);

    Boolean updateUserAvatar(String username, String avatar);

    Boolean checkPhoneUnique(SysUser user);

    Boolean checkEmailUnique(SysUser user);

}
