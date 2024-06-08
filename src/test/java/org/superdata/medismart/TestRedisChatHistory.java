package org.superdata.medismart;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.superdata.medismart.utils.database.RedisCache;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@SpringBootTest
public class TestRedisChatHistory {

    @Resource
    private RedisCache redisCache;

    @Test
    void testChatHistory() {
//        Collection<String> keys = redisCache.keys("message_store:test:1");
        Collection<String> keys = redisCache.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }
        List<Object> cacheList = redisCache.getCacheList("message_store:1:1");

        System.out.println(cacheList);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object jsonTree = mapper.readTree(cacheList.toString());
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            String formattedJson = writer.writeValueAsString(jsonTree);
            System.out.println(formattedJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
