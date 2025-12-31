package org.superdata.medismart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.superdata.medismart.base.BaseTest;
import org.superdata.medismart.entity.SysRole;
import org.superdata.medismart.entity.SysUser;
import org.superdata.medismart.mapper.sql.SysRoleMapper;
import org.superdata.medismart.mapper.sql.SysUserMapper;
import org.superdata.medismart.service.impl.SysUserServiceImpl;
import org.superdata.medismart.utils.TestDataFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * 用户服务测试类
 */
@SpringBootTest
@DisplayName("用户服务测试")
class SysUserServiceTest extends BaseTest {

    @Mock
    private SysUserMapper userMapper;

    @Mock
    private SysRoleMapper roleMapper;

    @InjectMocks
    private SysUserServiceImpl sysUserService;

    private SysUser testUser;

    @BeforeEach
    void setUp() {
        testUser = TestDataFactory.createTestUser();
    }

    @Test
    @DisplayName("获取用户角色组 - 用户拥有多个角色")
    void testSelectUserRoleGroup_withMultipleRoles() {
        // 准备测试数据
        Long userId = 1L;
        SysRole role1 = new SysRole();
        role1.setName("管理员");
        SysRole role2 = new SysRole();
        role2.setName("普通用户");
        List<SysRole> roles = Arrays.asList(role1, role2);

        // Mock行为
        when(roleMapper.selectRolesByUserId(userId)).thenReturn(roles);

        // 执行测试
        String result = sysUserService.selectUserRoleGroup(userId);

        // 验证结果
        assertEquals("管理员,普通用户", result);
        verify(roleMapper, times(1)).selectRolesByUserId(userId);
    }

    @Test
    @DisplayName("获取用户角色组 - 用户没有角色")
    void testSelectUserRoleGroup_withNoRoles() {
        // 准备测试数据
        Long userId = 1L;

        // Mock行为
        when(roleMapper.selectRolesByUserId(userId)).thenReturn(Collections.emptyList());

        // 执行测试
        String result = sysUserService.selectUserRoleGroup(userId);

        // 验证结果
        assertEquals("", result);
        verify(roleMapper, times(1)).selectRolesByUserId(userId);
    }

    @Test
    @DisplayName("获取用户角色组 - 用户只有一个角色")
    void testSelectUserRoleGroup_withSingleRole() {
        // 准备测试数据
        Long userId = 1L;
        SysRole role = new SysRole();
        role.setName("普通用户");
        List<SysRole> roles = Collections.singletonList(role);

        // Mock行为
        when(roleMapper.selectRolesByUserId(userId)).thenReturn(roles);

        // 执行测试
        String result = sysUserService.selectUserRoleGroup(userId);

        // 验证结果
        assertEquals("普通用户", result);
        verify(roleMapper, times(1)).selectRolesByUserId(userId);
    }

    @Test
    @DisplayName("重置用户密码 - 成功")
    void testResetUserPwd_success() {
        // 准备测试数据
        String userName = "testuser";
        String newPassword = "newPassword123";

        // Mock行为
        when(userMapper.updateUserPassword(userName, newPassword)).thenReturn(true);

        // 执行测试
        Boolean result = sysUserService.resetUserPwd(userName, newPassword);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).updateUserPassword(userName, newPassword);
    }

    @Test
    @DisplayName("重置用户密码 - 失败")
    void testResetUserPwd_failure() {
        // 准备测试数据
        String userName = "nonexistentuser";
        String newPassword = "newPassword123";

        // Mock行为
        when(userMapper.updateUserPassword(userName, newPassword)).thenReturn(false);

        // 执行测试
        Boolean result = sysUserService.resetUserPwd(userName, newPassword);

        // 验证结果
        assertFalse(result);
        verify(userMapper, times(1)).updateUserPassword(userName, newPassword);
    }

    @Test
    @DisplayName("更新用户头像 - 成功")
    void testUpdateUserAvatar_success() {
        // 准备测试数据
        String username = "testuser";
        String avatar = "/uploads/avatar.jpg";

        // Mock行为
        when(userMapper.updateUserAvatar(username, avatar)).thenReturn(true);

        // 执行测试
        Boolean result = sysUserService.updateUserAvatar(username, avatar);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).updateUserAvatar(username, avatar);
    }

    @Test
    @DisplayName("更新用户头像 - 失败")
    void testUpdateUserAvatar_failure() {
        // 准备测试数据
        String username = "nonexistentuser";
        String avatar = "/uploads/avatar.jpg";

        // Mock行为
        when(userMapper.updateUserAvatar(username, avatar)).thenReturn(false);

        // 执行测试
        Boolean result = sysUserService.updateUserAvatar(username, avatar);

        // 验证结果
        assertFalse(result);
        verify(userMapper, times(1)).updateUserAvatar(username, avatar);
    }

    @Test
    @DisplayName("检查手机号唯一性 - 手机号为空")
    void testCheckPhoneUnique_phoneNumberIsNull() {
        // 准备测试数据
        SysUser user = TestDataFactory.createTestUser();
        user.setPhonenumber(null);

        // 执行测试
        Boolean result = sysUserService.checkPhoneUnique(user);

        // 验证结果
        assertTrue(result);
        verify(userMapper, never()).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("检查手机号唯一性 - 手机号唯一")
    void testCheckPhoneUnique_phoneNumberIsUnique() {
        // 准备测试数据
        SysUser user = TestDataFactory.createTestUser();
        user.setPhonenumber("13900139000");

        // Mock行为
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // 执行测试
        Boolean result = sysUserService.checkPhoneUnique(user);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("检查手机号唯一性 - 手机号已存在但是同一用户")
    void testCheckPhoneUnique_phoneNumberExistsSameUser() {
        // 准备测试数据
        SysUser user = TestDataFactory.createTestUser();
        user.setId(1L);
        user.setPhonenumber("13900139000");

        SysUser existingUser = TestDataFactory.createTestUser();
        existingUser.setId(1L);
        existingUser.setPhonenumber("13900139000");

        // Mock行为
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existingUser);

        // 执行测试
        Boolean result = sysUserService.checkPhoneUnique(user);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("检查手机号唯一性 - 手机号已被其他用户使用")
    void testCheckPhoneUnique_phoneNumberExistsDifferentUser() {
        // 准备测试数据
        SysUser user = TestDataFactory.createTestUser();
        user.setId(1L);
        user.setPhonenumber("13900139000");

        SysUser existingUser = TestDataFactory.createTestUser();
        existingUser.setId(2L);
        existingUser.setPhonenumber("13900139000");

        // Mock行为
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existingUser);

        // 执行测试
        Boolean result = sysUserService.checkPhoneUnique(user);

        // 验证结果
        assertFalse(result);
        verify(userMapper, times(1)).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("检查邮箱唯一性 - 邮箱为空")
    void testCheckEmailUnique_emailIsNull() {
        // 准备测试数据
        SysUser user = TestDataFactory.createTestUser();
        user.setEmail(null);

        // 执行测试
        Boolean result = sysUserService.checkEmailUnique(user);

        // 验证结果
        assertTrue(result);
        verify(userMapper, never()).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("检查邮箱唯一性 - 邮箱唯一")
    void testCheckEmailUnique_emailIsUnique() {
        // 准备测试数据
        SysUser user = TestDataFactory.createTestUser();
        user.setEmail("unique@example.com");

        // Mock行为
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // 执行测试
        Boolean result = sysUserService.checkEmailUnique(user);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("检查邮箱唯一性 - 邮箱已存在但是同一用户")
    void testCheckEmailUnique_emailExistsSameUser() {
        // 准备测试数据
        SysUser user = TestDataFactory.createTestUser();
        user.setId(1L);
        user.setEmail("test@example.com");

        SysUser existingUser = TestDataFactory.createTestUser();
        existingUser.setId(1L);
        existingUser.setEmail("test@example.com");

        // Mock行为
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existingUser);

        // 执行测试
        Boolean result = sysUserService.checkEmailUnique(user);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("检查邮箱唯一性 - 邮箱已被其他用户使用")
    void testCheckEmailUnique_emailExistsDifferentUser() {
        // 准备测试数据
        SysUser user = TestDataFactory.createTestUser();
        user.setId(1L);
        user.setEmail("test@example.com");

        SysUser existingUser = TestDataFactory.createTestUser();
        existingUser.setId(2L);
        existingUser.setEmail("test@example.com");

        // Mock行为
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existingUser);

        // 执行测试
        Boolean result = sysUserService.checkEmailUnique(user);

        // 验证结果
        assertFalse(result);
        verify(userMapper, times(1)).selectOne(any(LambdaQueryWrapper.class));
    }
}
