package cn.ixiaopeng.vj.smart.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 流操作工具类
 * @author venus
 * @since 1.0.0
 * @version 1.1.0
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

    /**
     * 将输入流复制到输出流
     * @param inputStream 输入流
     * @param outputStream 输出流
     */
    public static void copyStream (InputStream inputStream, OutputStream outputStream) {
        try {
            int length;
            byte[] buffer = new byte[4 * 1024];
            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        } catch (IOException e) {
            LOGGER.error("Copy stream failure", e);
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                LOGGER.error("Close stream failure", e);
            }
        }
    }
}
