package cn.ixiaopeng.vj.smart.utils;

/**
 * 转型操作工具类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class CastUtil {
    /**
     * 转为String型（默认为空）
     * @param value 待转值
     * @return 转完值
     */
    public static String castString (Object value) {
        return CastUtil.castString(value, "");
    }

    /**
     * 转为String型（可指定默认值）
     * @param value 待转值
     * @param defaultValue 默认值
     * @return 转完值
     */
    public static String castString (Object value, String defaultValue) {
        return value != null ? String.valueOf(value) : defaultValue;
    }

    /**
     * 转为double型（默认值为0）
     * @param value 待转值
     * @return 转完值
     */
    public static double castDouble (Object value) {
        return CastUtil.castDouble(value, 0);
    }

    /**
     * 转为double型（可指定默认值）
     * @param value 待转值
     * @param defaultValue 默认值
     * @return 转完值
     */
    public static double castDouble (Object value, double defaultValue) {
        double doubleValue = defaultValue;
        if (value != null) {
            String strValue = castString(value);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    doubleValue = Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }

    /**
     * 转为long型（默认为0）
     * @param value 待转值
     * @return 转完值
     */
    public static long castLong (Object value) {
        return CastUtil.castLong(value, 0);
    }

    /**
     * 转为long型（可自定义默认值）
     * @param value 待转值
     * @param defaultValue 默认值
     * @return 转完值
     */
    public static long castLong (Object value, long defaultValue) {
        long longValue = defaultValue;
        if (value != null) {
            String strValue = castString(value);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    longValue = Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    /**
     * 转为int型（默认为0）
     * @param value 待转值
     * @return 转完值
     */
    public static int castInt (Object value) {
        return CastUtil.castInt(value, 0);
    }

    /**
     * 转为int型（可自定义默认值）
     * @param value 待转值
     * @param defaultValue 默认值
     * @return 转完值
      */
    public static int castInt (Object value, int defaultValue) {
        int intValue = defaultValue;
        if (value != null) {
            String strValue = castString(value);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    intValue = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    /**
     * 转为boolean型（默认为false）
     * @param value 待转值
     * @return 转完值
     */
    public static boolean castBoolean (Object value) {
        return CastUtil.castBoolean(value, false);
    }

    /**
     * 转为boolean型（可自定义默认值）
     * @param value 待转值
     * @param defaultValue 默认值
     * @return 转完值
     */
    public static boolean castBoolean (Object value, boolean defaultValue) {
        boolean boolValue = defaultValue;
        if (value != null) {
            boolValue = Boolean.parseBoolean(castString(value));
        }
        return boolValue;
    }

    /**
     * 将对象转为需要的具体对象
     * @param object 泛化对象
     * @param <T> 具体类型
     * @return 转完后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast (Object object) {
        return (T) object;
    }
}
