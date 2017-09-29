package cn.ixiaopeng.helper;

import cn.ixiaopeng.utils.CastUtil;
import cn.ixiaopeng.utils.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bean 助手类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class BeanHelper {
    // 定义Bean映射（Bean类和实例之间的映射）
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> cls : beanClassSet) {
            Object object = ReflectionUtil.newInstance(cls);
            BEAN_MAP.put(cls, object);
        }
    }

    public static Map<Class<?>, Object> getBeanMap () {
        return BEAN_MAP;
    }

    /**
     * 获取Bean实例
     * @param cls 类类型
     * @param <T> 待转换类型
     * @return 转换后的对象
     */
    public static <T> T getBean (Class<T> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("Can not get bean by class: " + cls);
        }
        return CastUtil.cast(BEAN_MAP.get(cls));
    }
}