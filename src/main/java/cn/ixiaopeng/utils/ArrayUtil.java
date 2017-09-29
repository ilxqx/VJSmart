package cn.ixiaopeng.utils;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 数组工具类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class ArrayUtil {
    /**
     * 判断数组是否为空
     * @param array 待判断数组
     * @return 布尔
     */
    public static boolean isEmpty (Object[] array) {
        return ArrayUtils.isEmpty(array);
    }

    /**
     * 判断数组是否非空
     * @param array 待判断数组
     * @return 布尔
     */
    public static boolean isNotEmpty (Object[] array) {
        return !isEmpty(array);
    }
}
