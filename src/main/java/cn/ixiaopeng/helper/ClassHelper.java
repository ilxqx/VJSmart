package cn.ixiaopeng.helper;

import cn.ixiaopeng.annotation.Controller;
import cn.ixiaopeng.annotation.Service;
import cn.ixiaopeng.utils.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手类
 * @author venus
 * @since 1.0.0
 * @version 1.1.0
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
     * 获取应用包下所有带有某注解的类
     * @return 类集合
     */
    public static Set<Class<?>> getClassSetByAnnotation (Class<? extends Annotation> annotation) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASSES) {
            if (cls.isAnnotationPresent(annotation)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包下某父类或接口的所有子类
     * @param superClass 父类或接口
     * @return 类集合
     */
    public static Set<Class<?>> getClassSetBySuper (Class<?> superClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASSES) {
            if (!superClass.equals(cls) && superClass.isAssignableFrom(cls)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包下所有Service类
     * @return 类集合
     */
    public static Set<Class<?>> getServiceClassSet () {
        return getClassSetByAnnotation(Service.class);
    }

    /**
     * 获取应用包下所有Controller类
     * @return 类集合
     */
    public static Set<Class<?>> getControllerClassSet () {
        return getClassSetByAnnotation(Controller.class);
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
