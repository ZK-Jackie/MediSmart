package org.superdata.medismart.utils.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.superdata.medismart.base.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT工具类测试
 */
@SpringBootTest
@DisplayName("JWT工具类测试")
class JwtUtilTest extends BaseTest {

    @Test
    @DisplayName("生成UUID")
    void testGetUUID() {
        String uuid = JwtUtil.getUUID();
        assertNotNull(uuid);
        assertFalse(uuid.isEmpty());
        assertFalse(uuid.contains("-"));
    }

    @Test
    @DisplayName("创建JWT - 只传subject")
    void testCreateJWT_onlySubject() {
        String subject = "test-user-123";
        String jwt = JwtUtil.createJWT(subject);
        
        assertNotNull(jwt);
        assertFalse(jwt.isEmpty());
        assertTrue(jwt.split("\\.").length == 3); // JWT应该有3个部分
    }

    @Test
    @DisplayName("创建JWT - 传subject和ttl")
    void testCreateJWT_withTTL() {
        String subject = "test-user-123";
        Long ttl = 3600000L; // 1小时
        String jwt = JwtUtil.createJWT(subject, ttl);
        
        assertNotNull(jwt);
        assertFalse(jwt.isEmpty());
    }

    @Test
    @DisplayName("创建JWT - 传id、subject和ttl")
    void testCreateJWT_withIdAndTTL() {
        String id = "custom-id-123";
        String subject = "test-user-123";
        Long ttl = 3600000L;
        String jwt = JwtUtil.createJWT(id, subject, ttl);
        
        assertNotNull(jwt);
        assertFalse(jwt.isEmpty());
    }

    @Test
    @DisplayName("解析JWT - 成功")
    void testParseJWT_success() throws Exception {
        // 创建JWT
        String subject = "test-user-123";
        String jwt = JwtUtil.createJWT(subject);
        
        // 解析JWT
        Claims claims = JwtUtil.parseJWT(jwt);
        
        assertNotNull(claims);
        assertEquals(subject, claims.getSubject());
        assertEquals("sg", claims.getIssuer());
        assertNotNull(claims.getId());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    @DisplayName("解析JWT - 验证过期时间")
    void testParseJWT_checkExpiration() throws Exception {
        // 创建JWT
        String subject = "test-user-123";
        Long ttl = 3600000L; // 1小时
        String jwt = JwtUtil.createJWT(subject, ttl);
        
        // 解析JWT
        Claims claims = JwtUtil.parseJWT(jwt);
        
        // 验证过期时间
        long now = System.currentTimeMillis();
        long expiration = claims.getExpiration().getTime();
        long diff = expiration - now;
        
        // 允许一定的误差范围（10秒）
        assertTrue(diff >= ttl - 10000 && diff <= ttl + 10000);
    }

    @Test
    @DisplayName("解析JWT - 验证自定义ID")
    void testParseJWT_customId() throws Exception {
        // 创建带自定义ID的JWT
        String customId = "my-custom-id";
        String subject = "test-user-123";
        String jwt = JwtUtil.createJWT(customId, subject, 3600000L);
        
        // 解析JWT
        Claims claims = JwtUtil.parseJWT(jwt);
        
        assertEquals(customId, claims.getId());
    }

    @Test
    @DisplayName("解析JWT - 无效token")
    void testParseJWT_invalidToken() {
        String invalidJwt = "invalid.jwt.token";
        
        assertThrows(Exception.class, () -> {
            JwtUtil.parseJWT(invalidJwt);
        });
    }

    @Test
    @DisplayName("生成密钥")
    void testGeneralKey() {
        assertNotNull(JwtUtil.generalKey());
        assertEquals("AES", JwtUtil.generalKey().getAlgorithm());
    }

    @Test
    @DisplayName("验证JWT时效性")
    void testJWT_timeValidity() throws Exception {
        // 创建一个极短过期时间的JWT（1毫秒）
        String subject = "test-user";
        Long ttl = 1L;
        String jwt = JwtUtil.createJWT(subject, ttl);
        
        // 等待超过过期时间
        waitFor(100);
        
        // 尝试解析已过期的JWT，应该抛出异常
        assertThrows(Exception.class, () -> {
            JwtUtil.parseJWT(jwt);
        });
    }

    @Test
    @DisplayName("多次生成JWT - 验证唯一性")
    void testCreateJWT_uniqueness() {
        String subject = "test-user-123";
        
        String jwt1 = JwtUtil.createJWT(subject);
        String jwt2 = JwtUtil.createJWT(subject);
        
        // 即使subject相同，生成的JWT也应该不同（因为UUID和时间戳不同）
        assertNotEquals(jwt1, jwt2);
    }

    @Test
    @DisplayName("解析JWT - 验证所有声明")
    void testParseJWT_allClaims() throws Exception {
        String subject = "test-user-456";
        String jwt = JwtUtil.createJWT(subject);
        
        Claims claims = JwtUtil.parseJWT(jwt);
        
        // 验证所有重要的声明
        assertNotNull(claims.getId(), "ID不应为空");
        assertEquals(subject, claims.getSubject(), "Subject应该匹配");
        assertEquals("sg", claims.getIssuer(), "Issuer应该是'sg'");
        assertNotNull(claims.getIssuedAt(), "签发时间不应为空");
        assertNotNull(claims.getExpiration(), "过期时间不应为空");
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()), 
                   "过期时间应该在签发时间之后");
    }
}
