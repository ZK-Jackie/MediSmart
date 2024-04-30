package org.superdata.medismart.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

public class ObjectUtil {

    public static Object createAndLoadObject(String className, Map<String, Object> properties) throws Exception {
        Object object = createObject(className);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            setProperty(object, entry.getKey(), entry.getValue());
        }
        return object;
    }

    public static Object createObject(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        return clazz.getDeclaredConstructor().newInstance();
    }

    public static  void setProperty(Object object, String propertyName, Object value) throws Exception {
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, object.getClass());
        Method setter = pd.getWriteMethod();
        setter.invoke(object, value);
    }
}
