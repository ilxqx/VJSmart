package cn.ixiaopeng.vj.smart.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 * @author venus
 * @since 1.0.0
 * @version 1.1.0
 */
public final class StringUtil {
    // 字符串分隔符
    public static final String SEPARATOR = String.valueOf((char)29);
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

    /**
     * 字符串分割
     * @param str 原字符串
     * @param separate 分割字符串
     * @return 字符串数组
     */
    public static String[] split (String str, String separate) {
        return StringUtils.split(str, separate);
    }
}
