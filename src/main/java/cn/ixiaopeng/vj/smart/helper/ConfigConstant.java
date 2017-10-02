package cn.ixiaopeng.vj.smart.helper;

/**
 * 配置常量类
 * @author venus
 * @since 1.0.0
 * @version 1.1.0
 */
public interface ConfigConstant {
    // 配置文件名
    String CONFIG_FILE = "vj.smart.properties";

    // 数据库配置名
    String JDBC_DRIVER = "vj.smart.jdbc.driver";
    String JDBC_URL = "vj.smart.jdbc.url";
    String JDBC_USERNAME = "vj.smart.jdbc.username";
    String JDBC_PASSWORD = "vj.smart.jdbc.password";

    // 应用配置名
    String APP_BASE_PACKAGE = "vj.smart.app.basePackage";
    String APP_JSP_PATH = "vj.smart.app.jspPath";
    String APP_ASSET_PATH = "vj.smart.app.assetPath";
    String APP_VIEW_REPLACE_STR_FILE_NAME = "vj.smart.app.viewReplaceStrFileName";
}
