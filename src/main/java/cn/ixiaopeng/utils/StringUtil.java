package cn.ixiaopeng.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class StringUtil {
    /**
     * 判断字符串是否为空
     * @param string 待判断字符串
     * @return true|false
     */
    public static boolean isEmpty (String string) {
        return StringUtils.isBlank(string);
    }

    /**
     * 判断字符串是否不为空
     * @param string 待判断字符串
     * @return true|false
     */
    public static boolean isNotEmpty (String string) {
        return !isEmpty(string);
    }
}
