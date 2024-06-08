package org.superdata.medismart;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import org.superdata.medismart.utils.database.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MediSmartServerApplicationTests {

    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    public static final Integer CAPTCHA_EXPIRATION = 2;

    @Resource
    private RedisCache redisCache;

    @Test
    void contextLoads() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }

    @Test
    void testCaptcha(){
        // 生成uuid
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString(true);
        System.out.println(uuidString);
        String verifyKey = CAPTCHA_CODE_KEY + uuidString;
        System.out.println(verifyKey);

        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        // 展示captcha
        String code = captcha.getCode();
        System.out.println(code);

        // 保存验证码信息
        redisCache.setCacheObject(verifyKey, code, CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        FastByteArrayOutputStream os = new FastByteArrayOutputStream();

        BufferedImage image = captcha.getImage();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (Exception e) {
            System.out.println(e);
        }

        String cacheObject = redisCache.getCacheObject(verifyKey);
        System.out.println(cacheObject);

        System.out.println(Base64.encode(os.toByteArray()));
    }
}
