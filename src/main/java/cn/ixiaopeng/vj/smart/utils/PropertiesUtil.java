package cn.ixiaopeng.vj.smart.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 加载属性配置文件
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class PropertiesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);
    /**
     * 加载属性文件
     */
    public static Parser loadProps (String fileName) {
        Properties properties = null;
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new FileNotFoundException(fileName + " file is not found");
            }
            properties = new Properties();
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            LOGGER.error(fileName + " file is not found", e);
        } catch (IOException e) {
            LOGGER.error("load properties file failure", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("close input stream failure", e);
                }
            }
        }
        return new Parser(properties);
    }

    /**
     * 属性集合静态类
     */
    public static class Parser {
        private Properties properties;

        /**
         * 构造方法
         * @param properties 属性对象
         */
        public Parser (Properties properties) {
            this.properties = properties;
        }

        /**
         * 获取字符型属性（默认值为空字符串）
         * @param key 属性键值
         * @return 属性值
         */
        public String getString (String key) {
            return getString(key, "");
        }

        /**
         * 获取字符型属性（可指定默认值）
         * @param key 属性键值
         * @param defaultValue 默认值
         * @return 属性值
         */
        public String getString (String key, String defaultValue) {
            String value = defaultValue;
            if (this.properties.containsKey(key)) {
                value = this.properties.getProperty(key);
            }
            return value;
        }

        /**
         * 获取数值型属性（默认值为0）
         * @param key 属性键值
         * @return 属性值
         */
        public int getInt (String key) {
            return getInt(key, 0);
        }

        /**
         * 获取字符型属性（可指定默认值）
         * @param key 属性键值
         * @param defaultValue 默认值
         * @return 属性值
         */
        public int getInt (String key, int defaultValue) {
            int value = defaultValue;
            if (this.properties.containsKey(key)) {
                value = CastUtil.castInt(this.properties.getProperty(key));
            }
            return value;
        }

        /**
         * 获取布尔类型属性（默认值为false）
         * @param key 属性键值
         * @return 属性值
         */
        public boolean getBoolean (String key) {
            return getBoolean(key, false);
        }

        /**
         * 获取布尔类型属性（可自定义属性值）
         * @param key 属性键值
         * @param defaultValue 默认值
         * @return 属性值
         */
        public boolean getBoolean (String key, boolean defaultValue) {
            boolean value = defaultValue;
            if (this.properties.containsKey(key)) {
                value = CastUtil.castBoolean(this.properties.getProperty(key));
            }
            return value;
        }

        /**
         * 获取所有的键值对
         * @return Map
         */
        public Map<String, Object> getMap () {
            Map<String, Object> map = new HashMap<String, Object>();
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                Object key = enumeration.nextElement();
                String keyStr = CastUtil.cast(key);
                map.put(keyStr, getString(keyStr));
            }
            return map;
        }
    }
}
