package org.superdata.medismart.mapper.sql;

import org.superdata.medismart.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 23918
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-04-16 11:58:25
* @Entity org.superdata.medismart.entity.SysRole
*/
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole>  selectRolesByUserId(Long id);
}




