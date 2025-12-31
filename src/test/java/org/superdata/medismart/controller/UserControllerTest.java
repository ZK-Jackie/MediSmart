package org.superdata.medismart.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.superdata.medismart.base.BaseTest;
import org.superdata.medismart.common.ResponseResult;
import org.superdata.medismart.common.exception.ServiceException;
import org.superdata.medismart.entity.SysUser;
import org.superdata.medismart.entity.request.UserLoginRequest;
import org.superdata.medismart.security.service.LoginService;
import org.superdata.medismart.utils.TestDataFactory;
import org.superdata.medismart.utils.database.RedisCache;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 用户控制器测试类
 */
@SpringBootTest
@DisplayName("用户控制器测试")
class UserControllerTest extends BaseTest {

    @Mock
    private LoginService loginService;

    @Mock
    private RedisCache redisCache;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private UserLoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        loginRequest = TestDataFactory.createLoginRequest();
    }

    @Test
    @DisplayName("登录成功")
    void testLogin_success() {
        // 准备测试数据
        Map<String, String> data = new HashMap<>();
        data.put("token", "test-jwt-token");
        ResponseResult expectedResult = new ResponseResult(200, "登陆成功", data);

        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn("1234");
        when(loginService.login(any(SysUser.class))).thenReturn(expectedResult);

        // 执行测试
        ResponseResult result = userController.login(loginRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("登陆成功", result.getMsg());
        
        @SuppressWarnings("unchecked")
        Map<String, String> resultData = (Map<String, String>) result.getData();
        assertTrue(resultData.containsKey("token"));

        // 验证方法调用
        verify(redisCache, times(1)).getCacheObject(anyString());
        verify(redisCache, times(1)).deleteObject(anyString());
        verify(loginService, times(1)).login(any(SysUser.class));
    }

    @Test
    @DisplayName("登录失败 - 参数为空")
    void testLogin_nullRequest() {
        // 执行测试
        ResponseResult result = userController.login(null);

        // 验证结果
        assertNotNull(result);
        assertEquals(401, result.getCode());
        assertEquals("参数不能为空", result.getMsg());

        // 验证loginService未被调用
        verify(loginService, never()).login(any(SysUser.class));
    }

    @Test
    @DisplayName("登录失败 - 验证码为空")
    void testLogin_captchaNull() {
        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn(null);

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userController.login(loginRequest);
        });

        assertEquals("验证码已失效", exception.getMessage());
        verify(loginService, never()).login(any(SysUser.class));
    }

    @Test
    @DisplayName("登录失败 - 验证码错误")
    void testLogin_captchaInvalid() {
        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn("5678");

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userController.login(loginRequest);
        });

        assertEquals("验证码错误", exception.getMessage());
        verify(loginService, never()).login(any(SysUser.class));
    }

    @Test
    @DisplayName("登录失败 - 用户名为空")
    void testLogin_usernameNull() {
        // 准备测试数据
        loginRequest.setUsername(null);

        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn("1234");

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userController.login(loginRequest);
        });

        assertEquals("用户名或密码不能为空", exception.getMessage());
        verify(loginService, never()).login(any(SysUser.class));
    }

    @Test
    @DisplayName("登录失败 - 密码为空")
    void testLogin_passwordNull() {
        // 准备测试数据
        loginRequest.setPassword(null);

        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn("1234");

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userController.login(loginRequest);
        });

        assertEquals("用户名或密码不能为空", exception.getMessage());
        verify(loginService, never()).login(any(SysUser.class));
    }

    @Test
    @DisplayName("登录失败 - 密码长度不符合要求(太短)")
    void testLogin_passwordTooShort() {
        // 准备测试数据
        loginRequest.setPassword("12345");

        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn("1234");

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userController.login(loginRequest);
        });

        assertEquals("密码长度不符合要求", exception.getMessage());
        verify(loginService, never()).login(any(SysUser.class));
    }

    @Test
    @DisplayName("登录失败 - 密码长度不符合要求(太长)")
    void testLogin_passwordTooLong() {
        // 准备测试数据
        loginRequest.setPassword("123456789012345678901");

        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn("1234");

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userController.login(loginRequest);
        });

        assertEquals("密码长度不符合要求", exception.getMessage());
        verify(loginService, never()).login(any(SysUser.class));
    }

    @Test
    @DisplayName("登录失败 - 用户名长度不符合要求(太短)")
    void testLogin_usernameTooShort() {
        // 准备测试数据
        loginRequest.setUsername("user");

        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn("1234");

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userController.login(loginRequest);
        });

        assertEquals("用户名长度不符合要求", exception.getMessage());
        verify(loginService, never()).login(any(SysUser.class));
    }

    @Test
    @DisplayName("登录失败 - 用户名长度不符合要求(太长)")
    void testLogin_usernameTooLong() {
        // 准备测试数据
        loginRequest.setUsername("usernamethatiswaytoolo");

        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn("1234");

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userController.login(loginRequest);
        });

        assertEquals("用户名长度不符合要求", exception.getMessage());
        verify(loginService, never()).login(any(SysUser.class));
    }

    @Test
    @DisplayName("注册成功")
    void testRegister_success() {
        // 准备测试数据
        ResponseResult expectedResult = new ResponseResult(200, "注册成功");

        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn("1234");
        when(loginService.register(any(SysUser.class))).thenReturn(expectedResult);

        // 执行测试
        ResponseResult result = userController.register(loginRequest);

        // 验证方法调用
        verify(redisCache, times(1)).getCacheObject(anyString());
        verify(redisCache, times(1)).deleteObject(anyString());
        verify(loginService, times(1)).register(any(SysUser.class));
    }

    @Test
    @DisplayName("注册失败 - 参数为空")
    void testRegister_nullRequest() {
        // 执行测试
        ResponseResult result = userController.register(null);

        // 验证结果
        assertNotNull(result);
        assertEquals(401, result.getCode());
        assertEquals("参数不能为空", result.getMsg());

        // 验证loginService未被调用
        verify(loginService, never()).register(any(SysUser.class));
    }

    @Test
    @DisplayName("注册失败 - 验证码错误")
    void testRegister_captchaInvalid() {
        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn("5678");

        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userController.register(loginRequest);
        });

        assertEquals("验证码错误", exception.getMessage());
        verify(loginService, never()).register(any(SysUser.class));
    }

    @Test
    @DisplayName("验证码校验 - 成功")
    void testValidateCaptcha_success() {
        // 准备测试数据
        String username = "testuser";
        String code = "1234";
        String uuid = "test-uuid";

        // Mock行为
        when(redisCache.getCacheObject(anyString())).thenReturn("1234");

        // 执行测试
        assertDoesNotThrow(() -> {
            userController.validateCaptcha(username, code, uuid);
        });

        verify(redisCache, times(1)).getCacheObject(anyString());
        verify(redisCache, times(1)).deleteObject(anyString());
    }

    @Test
    @DisplayName("登录前校验 - 成功")
    void testLoginPreCheck_success() {
        // 执行测试
        assertDoesNotThrow(() -> {
            userController.loginPreCheck("testuser", "123456");
        });
    }

    @Test
    @DisplayName("登录前校验 - 用户名为空")
    void testLoginPreCheck_usernameNull() {
        // 执行测试并验证异常
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userController.loginPreCheck(null, "123456");
        });

        assertEquals("用户名或密码不能为空", exception.getMessage());
    }
}
