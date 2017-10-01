package cn.ixiaopeng.vj.smart.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 流操作工具类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class StreamUtil {
    // 日志类
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    /**
     * 从输入流中获取字符串
     * @param inputStream 输入流
     * @return 字符串
     */
    public static String getString (InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("Get string failure", e);
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }
}
