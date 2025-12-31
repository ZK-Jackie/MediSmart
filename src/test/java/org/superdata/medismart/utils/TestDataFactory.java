package org.superdata.medismart.utils;

import org.superdata.medismart.entity.ChatConversation;
import org.superdata.medismart.entity.SysUser;
import org.superdata.medismart.entity.node.Disease;
import org.superdata.medismart.entity.request.UserChatRequest;
import org.superdata.medismart.entity.request.UserLoginRequest;

import java.util.Date;
import java.util.UUID;

/**
 * 测试数据工厂，用于创建测试所需的各种实体对象
 */
public class TestDataFactory {

    /**
     * 创建测试用户
     */
    public static SysUser createTestUser() {
        SysUser user = new SysUser();
        user.setId(1L);
        user.setUserName("testuser");
        user.setNickName("测试用户");
        user.setPassword("$2a$10$test.encrypted.password");
        user.setStatus("0");
        user.setEmail("test@example.com");
        user.setPhonenumber("13800138000");
        user.setSex("0");
        user.setUserType("1");
        user.setDelFlag(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return user;
    }

    /**
     * 创建登录请求
     */
    public static UserLoginRequest createLoginRequest() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("testuser");
        request.setPassword("123456");
        request.setCode("1234");
        request.setUuid(UUID.randomUUID().toString());
        return request;
    }

    /**
     * 创建管理员用户
     */
    public static SysUser createAdminUser() {
        SysUser user = createTestUser();
        user.setId(2L);
        user.setUserName("admin");
        user.setNickName("管理员");
        user.setUserType("0");
        return user;
    }

    /**
     * 创建聊天请求
     */
    public static UserChatRequest createChatRequest() {
        UserChatRequest request = new UserChatRequest();
        request.setUserId("1");
        request.setContent("你好，请介绍一下感冒的症状");
        request.setConversationId(UUID.randomUUID().toString());
        return request;
    }

    /**
     * 创建新会话的聊天请求
     */
    public static UserChatRequest createNewConversationChatRequest() {
        UserChatRequest request = new UserChatRequest();
        request.setUserId("1");
        request.setContent("你好，请介绍一下感冒的症状");
        return request;
    }

    /**
     * 创建聊天会话
     */
    public static ChatConversation createChatConversation() {
        ChatConversation conversation = new ChatConversation();
        conversation.setConversationId(UUID.randomUUID().toString());
        conversation.setUserId(1L);
        conversation.setConversationTitle("测试会话");
        conversation.setDelFlag(0);
        conversation.setCreateTime(new Date());
        conversation.setUpdateTime(new Date());
        return conversation;
    }

    /**
     * 创建疾病节点
     */
    public static Disease createDiseaseNode() {
        Disease disease = new Disease();
        disease.setId(1L);
        disease.setName("感冒");
        disease.setCategory("Disease");
        disease.setDesc("普通感冒是一种常见的呼吸道疾病");
        disease.setPrevent("注意保暖，多喝水");
        disease.setCause("病毒感染");
        disease.setEasyGet("儿童、老人、免疫力低下者");
        disease.setCureDepartment(java.util.Arrays.asList("呼吸内科"));
        disease.setCureWay(java.util.Arrays.asList("药物治疗", "休息"));
        disease.setCureLasttime("7-10天");
        disease.setCuredProb("95%");
        return disease;
    }

    /**
     * 创建带特定用户名的用户
     */
    public static SysUser createUserWithUsername(String username) {
        SysUser user = createTestUser();
        user.setUserName(username);
        return user;
    }

    /**
     * 创建带特定ID的用户
     */
    public static SysUser createUserWithId(Long id) {
        SysUser user = createTestUser();
        user.setId(id);
        return user;
    }

    /**
     * 创建已删除的用户
     */
    public static SysUser createDeletedUser() {
        SysUser user = createTestUser();
        user.setDelFlag(1);
        return user;
    }

    /**
     * 创建停用状态的用户
     */
    public static SysUser createDisabledUser() {
        SysUser user = createTestUser();
        user.setStatus("1");
        return user;
    }
}
