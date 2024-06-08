package org.superdata.medismart.mapper.sql;

import org.apache.ibatis.annotations.Param;
import org.superdata.medismart.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 23918
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-04-15 21:06:09
*/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    void insertUser(SysUser user);

    Boolean updateUserPassword(@Param("userName")String userName, @Param("password")String newPassword);

    Boolean updateUserAvatar(@Param("userName")String username, @Param("password")String avatar);
}




