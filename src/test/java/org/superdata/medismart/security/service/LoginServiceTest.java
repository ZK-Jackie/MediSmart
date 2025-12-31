package org.superdata.medismart.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.superdata.medismart.base.BaseTest;
import org.superdata.medismart.common.ResponseResult;
import org.superdata.medismart.common.constant.CacheConstants;
import org.superdata.medismart.common.exception.ServiceException;
import org.superdata.medismart.entity.SysUser;
import org.superdata.medismart.mapper.sql.SysUserMapper;
import org.superdata.medismart.security.domain.LoginUser;
import org.superdata.medismart.service.SysUserService;
import org.superdata.medismart.utils.TestDataFactory;
import org.superdata.medismart.utils.database.RedisCache;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 登录服务测试类
 */
@SpringBootTest
@DisplayName("登录服务测试")
class LoginServiceTest extends BaseTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RedisCache redisCache;

    @Mock
    private SysUserMapper sysUserMapper;

    @Mock
    private SysUserService sysUserService;

    @InjectMocks
    private LoginServiceImpl loginService;

    private SysUser testUser;
    private LoginUser loginUser;

    @BeforeEach
    void setUp() {
        testUser = TestDataFactory.createTestUser();
        loginUser = new LoginUser();
        loginUser.setUser(testUser);
    }

    @Test
    @DisplayName("登录成功")
    void testLogin_success() {
        // 准备测试数据
        SysUser user = new SysUser();
        user.setUserName("testuser");
        user.setPassword("123456");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(loginUser);

        // Mock行为
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // 执行测试
        ResponseResult result = loginService.login(user);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("登陆成功", result.getMsg());
        
        @SuppressWarnings("unchecked")
        Map<String, String> data = (Map<String, String>) result.getData();
        assertNotNull(data);
        assertTrue(data.containsKey("token"));
        assertNotNull(data.get("token"));

        // 验证Redis缓存操作
        verify(redisCache, times(1))
                .setCacheObject(eq(CacheConstants.USER_LOGIN_KEY + testUser.getId()), eq(loginUser));
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("登录失败 - 用户名或密码错误")
    void testLogin_badCredentials() {
        // 准备测试数据
        SysUser user = new SysUser();
        user.setUserName("testuser");
        user.setPassword("wrongpassword");

        // Mock行为 - 模拟认证失败
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("用户名或密码错误"));

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loginService.login(user);
        });

        assertEquals("用户名或密码错误", exception.getMessage());
        
        // 验证Redis缓存未被调用
        verify(redisCache, never()).setCacheObject(anyString(), any());
    }

    @Test
    @DisplayName("登录失败 - 认证返回null")
    void testLogin_authenticationNull() {
        // 准备测试数据
        SysUser user = new SysUser();
        user.setUserName("testuser");
        user.setPassword("123456");

        // Mock行为 - 模拟认证返回null
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            loginService.login(user);
        });

        assertEquals("用户名或密码错误", exception.getMessage());
        
        // 验证Redis缓存未被调用
        verify(redisCache, never()).setCacheObject(anyString(), any());
    }

    @Test
    @DisplayName("退出登录成功")
    void testLogout_success() {
        // 准备测试数据
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(loginUser);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // 执行测试
        ResponseResult result = loginService.logout();

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("退出成功", result.getMsg());

        // 验证Redis删除操作
        verify(redisCache, times(1))
                .deleteObject(CacheConstants.USER_LOGIN_KEY + testUser.getId());
    }

    @Test
    @DisplayName("用户注册")
    void testRegister() {
        // 准备测试数据
        SysUser user = new SysUser();
        user.setUserName("newuser");
        user.setPassword("123456");

        // Mock行为
        doNothing().when(sysUserMapper).insertUser(any(SysUser.class));

        // 执行测试
        ResponseResult result = loginService.register(user);

        // 验证密码已被加密
        assertNotEquals("123456", user.getPassword());
        assertTrue(user.getPassword().startsWith("$2a$"));

        // 验证插入操作被调用
        verify(sysUserMapper, times(1)).insertUser(any(SysUser.class));
    }

    @Test
    @DisplayName("用户注册 - 用户名已存在")
    void testRegister_usernameExists() {
        // 准备测试数据
        SysUser user = new SysUser();
        user.setUserName("existinguser");
        user.setPassword("123456");

        // Mock行为 - 模拟插入失败
        doThrow(new RuntimeException("用户名已存在"))
                .when(sysUserMapper).insertUser(any(SysUser.class));

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            loginService.register(user);
        });
    }
}
