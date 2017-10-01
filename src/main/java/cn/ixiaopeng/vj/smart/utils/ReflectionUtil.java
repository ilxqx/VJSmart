package cn.ixiaopeng.vj.smart.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class ReflectionUtil {
    // 日志类
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建实例
     * @param cls 类类型
     * @return 创建的实例
     */
    public static Object newInstance (Class<?> cls) {
        Object instance;
        try {
            instance = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("New instance failure", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 调用方法
     * @param object 方法的宿主对象
     * @param method 方法对象
     * @param args 方法参数
     * @return 方法返回结果
     */
    public static Object invokeMethod (Object object, Method method, Object... args) {
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Invoke method failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量
     * @param object 属性宿主对象
     * @param field 属相对象
     * @param value 属相待设定的值
     */
    public static void setField (Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            LOGGER.error("Set field failure", e);
            throw new RuntimeException(e);
        }
    }
}
