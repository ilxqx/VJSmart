package cn.ixiaopeng.vj.smart.helper;

import cn.ixiaopeng.vj.smart.utils.PropertiesUtil;

/**
 * 配置文件助手类
 * @author venus
 * @since 1.0.0
 * @version 1.2.0
 */
public final class ConfigHelper {
    // 整个应用配置加载项
    private static final PropertiesUtil.Parser CONFIG_PROPERTIES;

    /**
     * 静态初始化块
     */
    static {
        CONFIG_PROPERTIES = PropertiesUtil.loadProps(ConfigConstant.CONFIG_FILE);
        if (CONFIG_PROPERTIES == null) {
            throw new RuntimeException("Config file load failure and app can't startup");
        }
    }
    /**
     * 获取jdbc驱动
     * @return 返回jdbc驱动类名
     */
    public static String getJdbcDriver () {
        return CONFIG_PROPERTIES.getString(ConfigConstant.JDBC_DRIVER);
    }

    /**
     * 获取jdbc URL
     * @return 返回url字符串
     */
    public static String getJdbcUrl () {
        return CONFIG_PROPERTIES.getString(ConfigConstant.JDBC_URL);
    }

    /**
     * 获取jdbc用户名
     * @return 返回用户名
     */
    public static String getJdbcUsername () {
        return CONFIG_PROPERTIES.getString(ConfigConstant.JDBC_USERNAME);
    }

    /**
     * 获取jdbc密码
     * @return 返回密码
     */
    public static String getJdbcPassword () {
        return CONFIG_PROPERTIES.getString(ConfigConstant.JDBC_PASSWORD);
    }

    /**
     * 获取应用基础包名
     * @return 返回包名
     */
    public static String getAppBasePackage () {
        return CONFIG_PROPERTIES.getString(ConfigConstant.APP_BASE_PACKAGE);
    }

    /**
     * 获取应用JSP路径
     * @return JSP路径
     */
    public static String getAppJspPath () {
        return CONFIG_PROPERTIES.getString(ConfigConstant.APP_JSP_PATH, "/WEB-INF/view/");
    }

    /**
     * 获取应用静态资源路径
     * @return 静态资源路径
     */
    public static String getAppAssetPath () {
        return CONFIG_PROPERTIES.getString(ConfigConstant.APP_ASSET_PATH, "/asset/");
    }

    /**
     * 获取应用视图替换文件名
     * @return String 文件名
     */
    public static String getAppViewReplaceStrFileName () {
        return CONFIG_PROPERTIES.getString(ConfigConstant.APP_VIEW_REPLACE_STR_FILE_NAME, "view.replace.str.properties");
    }

    /**
     * 获取文件上传最大限制（默认为10M）
     * @return int
     */
    public static int getAppUploadLimit () {
        return CONFIG_PROPERTIES.getInt(ConfigConstant.APP_UPLOAD_LIMIT, 10);
    }
}
