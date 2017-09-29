package cn.ixiaopeng.helper;

import cn.ixiaopeng.annotation.Controller;
import cn.ixiaopeng.annotation.Service;
import cn.ixiaopeng.utils.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class ClassHelper {
    // 存放所有的类
    private static final Set<Class<?>> CLASSES;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASSES = ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取应用下所有的类
     * @return Set类集合
     */
    public static Set<Class<?>> getClassSet () {
        return CLASSES;
    }

    /**
     * 获取应用包下所有Service类
     * @return 类集合
     */
    public static Set<Class<?>> getServiceClassSet () {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASSES) {
            if (cls.isAnnotationPresent(Service.class)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包下所有Controller类
     * @return 类集合
     */
    public static Set<Class<?>> getControllerClassSet () {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASSES) {
            if (cls.isAnnotationPresent(Controller.class)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包下所有Bean类
     * @return 类集合
     */
    public static Set<Class<?>> getBeanClassSet () {
        Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControllerClassSet());
        return beanClassSet;
    }
}
