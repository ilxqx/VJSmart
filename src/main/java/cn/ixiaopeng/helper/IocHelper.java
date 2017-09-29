package cn.ixiaopeng.helper;

import cn.ixiaopeng.annotation.Inject;
import cn.ixiaopeng.utils.ArrayUtil;
import cn.ixiaopeng.utils.CollectionUtil;
import cn.ixiaopeng.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public class IocHelper {
    static {
        // 获取所有Bean实例的映射
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            // 遍历beanMap
            for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
                // 获取bean类和其实例
                Class<?> beanClass = entry.getKey();
                Object beanInstance = entry.getValue();
                // 获取bean类所有属性
                Field[] fields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(fields)) {
                    // 遍历bean fields
                    for (Field field : fields) {
                        // 判断该属是否有Inject注解
                        if (field.isAnnotationPresent(Inject.class)) {
                            // 获取该属性的类型
                            Class<?> fieldTypeClass = field.getType();
                            // 获取实例
                            Object fieldInstance = BeanHelper.getBean(fieldTypeClass);
                            if (fieldInstance != null) {
                                // 通过反射设置属性值
                                ReflectionUtil.setField(beanInstance, field, fieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}