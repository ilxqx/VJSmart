package cn.ixiaopeng.vj.smart.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 文件操作工具类
 * @author venus
 * @since 1.3.0
 * @version 1.0.0
 */
public final class FileUtil {
    // 日志类
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取真实文件名（自动去掉文件路径）
     * @param fileName 文件（带路径）名
     * @return 文件名
     */
    public static String getRealFileName (String fileName) {
        return FilenameUtils.getName(fileName);
    }

    /**
     * 创建文件
     * @param filePath 文件路径
     * @return 文件对象
     */
    public static File createFile (String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                FileUtils.forceMkdir(parentFile);
            }
        } catch (IOException e) {
            LOGGER.error("Create file failure", e);
            throw new RuntimeException(e);
        }
        return file;
    }
}
