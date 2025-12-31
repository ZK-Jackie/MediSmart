package org.superdata.medismart.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectUtils {

    /**
     * 根据类名创建对象，并加载属性
     *
     * @param className 类名，应提供全路径，如：org.superdata.medismart.entity.ChatMessage
     * @param properties 属性集合
     * @return Object 返回创建的对象
     * */
    public static Object createAndLoadObject(String className, Map<String, Object> properties) throws Exception {
        Object object = createObject(className);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            setProperty(object, entry.getKey(), entry.getValue());
        }
        return object;
    }

    /**
     * 根据类名创建对象
     *
     * @param className 类名，应提供全路径，如：org.superdata.medismart.entity.ChatMessage
     * @return Object 返回创建的对象
     * */
    public static Object createObject(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        return clazz.getDeclaredConstructor().newInstance();
    }

    /**
     * 设置对象属性
     *
     * @param object 对象
     * @param propertyName 属性名
     * @param value 属性值
     * */
    public static void setProperty(Object object, String propertyName, Object value) throws Exception {
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, object.getClass());
        Method setter = pd.getWriteMethod();
        setter.invoke(object, value);
    }

    /**
     * 获得对象属性
     *
     * @param object 对象
     * @param propertyName 属性名
     * @return Object 返回属性值
     * */
    public static Object getProperty(Object object, String propertyName) throws Exception {
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, object.getClass());
        Method getter = pd.getReadMethod();
        return getter.invoke(object);
    }

    /**
     * 类型转换
     *
     * @param className 类名，应提供全路径，如：org.superdata.medismart.entity.ChatMessage
     * @param obj 被转换的对象
     * @return Object 返回转换后的对象
     * */
    public static Object cast(String className, Object obj) throws Exception {
        Class<?> clazz = Class.forName(className);
        return clazz.cast(obj);
    }

    /**
     * 将 LinkedHashMap 转换为对象
     * @param map LinkedHashMap 对象
     * @param className 类名，应提供全路径，如：org.superdata.medismart.entity.ChatMessage
     * @return Object 返回转换后的对象
     * @throws Exception 异常
     */
    public static Object mapToObject(LinkedHashMap<String, Object> map, String className) throws Exception {
        // 创建新对象
        Object object = createObject(className);

        // 遍历 map 的每一个条目
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            // 尝试设置对象的属性
            try {
                setProperty(object, entry.getKey(), entry.getValue());
            } catch (Exception e) {
                // 如果属性不存在或者类型不匹配，忽略错误
            }
        }

        return object;
    }
}
