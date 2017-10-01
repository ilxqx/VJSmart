package cn.ixiaopeng.vj.smart.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * JSON工具类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class JsonUtil {
    // 日志类
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    // 对象映射（和JSON）
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将POJO转为JSON
     * @param object POJO对象
     * @param <T> 对象类型
     * @return JSON字符串
     */
    public static <T> String toJson (T object) {
        String  json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("Convert POJO to JSON failure", e);
            throw new RuntimeException(e);
        }
        return json;
    }

    /**
     * 将JSON转为POJO
     * @param json json字符串
     * @param typeCls 待转对象类类型
     * @param <T> 类型
     * @return POJO对象
     */
    public static <T> T toPojo (String json, Class<T> typeCls) {
        T pojo;
        try {
            pojo = OBJECT_MAPPER.readValue(json, typeCls);
        } catch (IOException e) {
            LOGGER.error("Convert JSON to POJO failure", e);
            throw new RuntimeException(e);
        }
        return pojo;
    }
}
