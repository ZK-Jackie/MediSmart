package org.superdata.medismart.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.superdata.medismart.base.BaseTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 字符串工具类测试
 */
@SpringBootTest
@DisplayName("字符串工具类测试")
class StringUtilsTest extends BaseTest {

    @Test
    @DisplayName("格式化文本 - 正常格式化")
    void testFormat_normal() {
        String template = "this is {} for {}";
        String result = StringUtils.format(template, "a", "b");
        assertEquals("this is a for b", result);
    }

    @Test
    @DisplayName("格式化文本 - 单个参数")
    void testFormat_singleParam() {
        String template = "Hello {}!";
        String result = StringUtils.format(template, "World");
        assertEquals("Hello World!", result);
    }

    @Test
    @DisplayName("格式化文本 - 多个参数")
    void testFormat_multipleParams() {
        String template = "{} + {} = {}";
        String result = StringUtils.format(template, 1, 2, 3);
        assertEquals("1 + 2 = 3", result);
    }

    @Test
    @DisplayName("格式化文本 - 空参数")
    void testFormat_emptyParams() {
        String template = "this is {} for {}";
        String result = StringUtils.format(template);
        assertEquals(template, result);
    }

    @Test
    @DisplayName("格式化文本 - 空模板")
    void testFormat_emptyTemplate() {
        String result = StringUtils.format(null, "a", "b");
        assertNull(result);
    }

    @Test
    @DisplayName("判断对象数组为空 - 数组为null")
    void testIsEmpty_objectArrayNull() {
        Object[] objects = null;
        assertTrue(StringUtils.isEmpty(objects));
    }

    @Test
    @DisplayName("判断对象数组为空 - 数组长度为0")
    void testIsEmpty_objectArrayEmpty() {
        Object[] objects = new Object[0];
        assertTrue(StringUtils.isEmpty(objects));
    }

    @Test
    @DisplayName("判断对象数组不为空")
    void testIsEmpty_objectArrayNotEmpty() {
        Object[] objects = new Object[]{1, 2, 3};
        assertFalse(StringUtils.isEmpty(objects));
    }

    @Test
    @DisplayName("判断字符串为空 - null")
    void testIsEmpty_stringNull() {
        String str = null;
        assertTrue(StringUtils.isEmpty(str));
    }

    @Test
    @DisplayName("判断字符串为空 - 空字符串")
    void testIsEmpty_stringEmpty() {
        String str = "";
        assertTrue(StringUtils.isEmpty(str));
    }

    @Test
    @DisplayName("判断字符串为空 - 只有空格")
    void testIsEmpty_stringBlank() {
        String str = "   ";
        assertTrue(StringUtils.isEmpty(str));
    }

    @Test
    @DisplayName("判断字符串不为空")
    void testIsEmpty_stringNotEmpty() {
        String str = "hello";
        assertFalse(StringUtils.isEmpty(str));
    }

    @Test
    @DisplayName("判断Collection为空 - null")
    void testIsEmpty_collectionNull() {
        List<String> list = null;
        assertTrue(StringUtils.isEmpty(list));
    }

    @Test
    @DisplayName("判断Collection为空 - 空集合")
    void testIsEmpty_collectionEmpty() {
        List<String> list = new ArrayList<>();
        assertTrue(StringUtils.isEmpty(list));
    }

    @Test
    @DisplayName("判断Collection不为空")
    void testIsEmpty_collectionNotEmpty() {
        List<String> list = Arrays.asList("a", "b", "c");
        assertFalse(StringUtils.isEmpty(list));
    }

    @Test
    @DisplayName("判断对象为空")
    void testIsNull_objectNull() {
        Object obj = null;
        assertTrue(StringUtils.isNull(obj));
    }

    @Test
    @DisplayName("判断对象不为空")
    void testIsNull_objectNotNull() {
        Object obj = new Object();
        assertFalse(StringUtils.isNull(obj));
    }

    @Test
    @DisplayName("数字左补0 - 正常情况")
    void testPadl_number() {
        String result = StringUtils.padl(123, 5);
        assertEquals("00123", result);
    }

    @Test
    @DisplayName("数字左补0 - 长度相等")
    void testPadl_numberSameLength() {
        String result = StringUtils.padl(12345, 5);
        assertEquals("12345", result);
    }

    @Test
    @DisplayName("字符串首字母大写")
    void testCapitalizeFirstLetter() {
        assertEquals("Disease", StringUtils.capitalizeFirstLetter("disease"));
        assertEquals("Disease", StringUtils.capitalizeFirstLetter("Disease"));
        assertEquals("D", StringUtils.capitalizeFirstLetter("d"));
    }

    @Test
    @DisplayName("字符串首字母小写")
    void testUncapitalize() {
        assertEquals("disease", StringUtils.uncapitalize("Disease"));
        assertEquals("disease", StringUtils.uncapitalize("disease"));
        assertEquals("d", StringUtils.uncapitalize("D"));
    }
}
