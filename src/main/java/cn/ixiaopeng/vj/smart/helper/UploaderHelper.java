package cn.ixiaopeng.vj.smart.helper;

import cn.ixiaopeng.vj.smart.core.FileParam;
import cn.ixiaopeng.vj.smart.core.FormParam;
import cn.ixiaopeng.vj.smart.core.Params;
import cn.ixiaopeng.vj.smart.utils.CollectionUtil;
import cn.ixiaopeng.vj.smart.utils.FileUtil;
import cn.ixiaopeng.vj.smart.utils.StreamUtil;
import cn.ixiaopeng.vj.smart.utils.StringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件上传助手类
 * @author venus
 * @since 1.3.0
 * @version 1.0.0
 */
public final class UploaderHelper {
    // 日之类
    private static final Logger LOGGER = LoggerFactory.getLogger(UploaderHelper.class);

    // Apache Commons FileUpload提供的Servlet文件上传对象
    private static ServletFileUpload servletFileUpload;

    /**
     * 上传文件初始化
     * @param servletContext servlet上下文对象
     */
    public static void init (ServletContext servletContext) {
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
        int uploadLimit = ConfigHelper.getAppUploadLimit();
        if (uploadLimit <= 0) {
            servletFileUpload.setFileSizeMax(10 * 1024 * 1024);
        } else {
            servletFileUpload.setFileSizeMax(uploadLimit * 1024 * 1024);
        }
    }

    /**
     * 判断请求是否为multipart类型
     * @param request 请求对象
     * @return 布尔
     */
    public static boolean isMultipart (HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 创建请求参数对象
     * @param request 请求对象
     * @return Params对象
     * @throws IOException 抛出异常
     */
    public static Params createParams (HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();
        List<FileParam> fileParamList = new ArrayList<FileParam>();
        try {
            Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
            if (CollectionUtil.isNotEmpty(fileItemListMap)) {
                for (Map.Entry<String, List<FileItem>> entry : fileItemListMap.entrySet()) {
                    String fieldName = entry.getKey();
                    List<FileItem> fileItemList = entry.getValue();
                    if (CollectionUtil.isNotEmpty(fileItemList)) {
                        for (FileItem fileItem : fileItemList) {
                            if (fileItem.isFormField()) {
                                // 表单参数
                                formParamList.add(new FormParam(fieldName, fileItem.getString("UTF-8")));
                            } else {
                                // 文件参数
                                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(), "UTF-8"));
                                if (StringUtil.isNotEmpty(fileName)) {
                                    fileParamList.add(new FileParam(fieldName, fileName, fileItem.getSize(), fileItem.getContentType(), fileItem.getInputStream()));
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            LOGGER.error("Create param failure", e);
            throw new RuntimeException(e);
        }
        return new Params(formParamList, fileParamList);
    }

    /**
     * 上传文件
     * @param basePath 上传文件的路径
     * @param fileParam 文件对象
     */
    public static void uploadFile (String basePath, FileParam fileParam) {
        try {
            if (fileParam != null) {
                String filePath = basePath + fileParam.getFileName();
                FileUtil.createFile(filePath);
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamUtil.copyStream(inputStream, outputStream);
            }
        } catch (IOException e) {
            LOGGER.error("Upload file failure", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量文件上传
     * @param basePath 基本路径
     * @param fileParamList 文件对象列表
     */
    public static void uploadFiles (String basePath, List<FileParam> fileParamList) {
        try {
            if (CollectionUtil.isNotEmpty(fileParamList)) {
                for (FileParam fileParam : fileParamList) {
                    uploadFile(basePath, fileParam);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Upload file failure", e);
            throw new RuntimeException(e);
        }
    }
}
