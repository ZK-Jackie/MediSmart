package org.superdata.medismart.base;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 测试基类，提供统一的测试配置
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public abstract class BaseTest {
    
    /**
     * 等待一段时间，用于异步操作测试
     * @param milliseconds 毫秒数
     */
    protected void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
