package cn.ixiaopeng.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class CollectionUtil {
    /**
     * 判断集合是否为空
     * @param collection 待判断集合
     * @return 布尔
     */
    public static boolean isEmpty (Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    /**
     * 判断集合是否非空
     * @param collection 待判断集合
     * @return 布尔
     */
    public static boolean isNotEmpty (Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是否为空
     * @param map 待判断map
     * @return 布尔
     */
    public static boolean isEmpty (Map<?, ?> map) {
        return MapUtils.isEmpty(map);
    }

    /**
     * 判断map是否非空
     * @param map 待判断map
     * @return 布尔
     */
    public static boolean isNotEmpty (Map<?, ?> map) {
        return !isEmpty(map);
    }
}
