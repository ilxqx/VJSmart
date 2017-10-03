package cn.ixiaopeng.vj.smart.core;

import cn.ixiaopeng.vj.smart.utils.CastUtil;
import cn.ixiaopeng.vj.smart.utils.CollectionUtil;
import cn.ixiaopeng.vj.smart.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求参数对象
 * @author venus
 * @since 1.0.0
 * @version 1.2.0
 */
public class Params {
    // 表单参数
    private List<FormParam> formParamList;
    // 文件参数
    private List<FileParam> fileParamList;

    /**
     * 构造函数
     * @param formParamList 表单参数列表
     * @param fileParamList 文件参数列表
     */
    public Params (List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    /**
     * 构造函数
     * @param formParamList 表单参数列表
     */
    public Params (List<FormParam> formParamList) {
        this(formParamList, null);
    }

    /**
     * 获取请求参数映射
     * @return Map
     */
    public Map<String, Object> getFieldMap () {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        if (CollectionUtil.isNotEmpty(formParamList)) {
            for (FormParam formParam : formParamList) {
                String fieldName = formParam.getFieldName();
                Object fieldValue = formParam.getFieldValue();
                if (fieldMap.containsKey(fieldName)) {
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
                }
                fieldMap.put(fieldName, fieldValue);
            }
        }
        return fieldMap;
    }

    /**
     * 获取上传文件映射
     * @return Map
     */
    public Map<String, List<FileParam>> getFileMap () {
        Map<String, List<FileParam>> fileMap = new HashMap<String, List<FileParam>>();
        if (CollectionUtil.isNotEmpty(fileParamList)) {
            for (FileParam fileParam : fileParamList) {
                String fieldName = fileParam.getFieldName();
                List<FileParam> fileParamList;
                if (fileMap.containsKey(fieldName)) {
                    fileParamList = fileMap.get(fieldName);
                } else {
                    fileParamList = new ArrayList<FileParam>();
                }
                fileParamList.add(fileParam);
                fileMap.put(fieldName, fileParamList);
            }
        }
        return fileMap;
    }

    /**
     * 获取所有上传文件
     * @param fieldName 字段名
     * @return List
     */
    public List<FileParam> getFileList (String fieldName) {
        return getFileMap().get(fieldName);
    }

    /**
     * 获取唯一上传文件
     * @param fieldName 字段名
     * @return 文件封装类或null
     */
    public FileParam getFile (String fieldName) {
        List<FileParam> fileParamList = getFileList(fieldName);
        if (CollectionUtil.isNotEmpty(fileParamList) && fileParamList.size() == 1) {
            return fileParamList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 判断参数是否为空
     * @return 布尔
     */
    public boolean isEmpty () {
        return CollectionUtil.isEmpty(formParamList) && CollectionUtil.isEmpty(fileParamList);
    }

    /**
     * 根据参数名获取String型参数值
     * @param name 名字
     * @return 参数值
     */
    public String getString (String name) {
        return CastUtil.castString(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取Double型参数值
     * @param name 名字
     * @return 参数值
     */
    public double getDouble (String name) {
        return CastUtil.castDouble(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取Long型参数值
     * @param name 名字
     * @return 参数值
     */
    public long getLong (String name) {
        return CastUtil.castLong(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取Int型参数值
     * @param name 名字
     * @return 参数值
     */
    public int getInt (String name) {
        return CastUtil.castInt(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取Boolean型参数值
     * @param name 名字
     * @return 参数值
     */
    public boolean getBoolean (String name) {
        return CastUtil.castBoolean(getFieldMap().get(name));
    }
}
