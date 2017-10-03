package cn.ixiaopeng.vj.smart.core;

import java.io.InputStream;

/**
 * 封装上传文件参数
 * @author venus
 * @since 1.3.0
 * @version 1.0.0
 */
public class FileParam {
    // 文件在表单中的名字
    private String fieldName;
    // 文件自己的名字
    private String fileName;
    // 文件的大小
    private long fileSize;
    // 文件的ContentType类型
    private String contentType;
    // 文件的输入流
    private InputStream inputStream;

    public FileParam (String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
