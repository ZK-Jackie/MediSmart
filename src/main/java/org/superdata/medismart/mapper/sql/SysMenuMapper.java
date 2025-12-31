package org.superdata.medismart.mapper.sql;

import org.superdata.medismart.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 23918
* @description 针对表【sys_menu(菜单表)】的数据库操作Mapper
* @createDate 2024-04-16 11:58:03
* @Entity org.superdata.medismart.entity.SysMenu
*/

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<String> selectPermsByUserId(Long id);
}




