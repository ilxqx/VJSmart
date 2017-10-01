package cn.ixiaopeng.vj.smart.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码解码工具类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class CodecUtil {
    // 日志
    private static final Logger LOGGER = LoggerFactory.getLogger(CodecUtil.class);

    /**
     * 将URL编码
     * @param source 原url
     * @return 编码后的url
     */
    public static String encodeUrl (String source) {
        String target;
        try {
            target = URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Encode url failure", e);
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * 将URL解码
     * @param source 原url
     * @return 解码后的url
     */
    public static String decodeUrl (String source) {
        String target;
        try {
            target = URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Decode url failure", e);
            throw new RuntimeException(e);
        }
        return target;
    }
}
