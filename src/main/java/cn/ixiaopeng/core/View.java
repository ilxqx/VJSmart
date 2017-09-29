package cn.ixiaopeng.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 试图对象
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public class View {
    // 视图路径
    private String path;

    // 视图展示数据
    private Map<String, Object> dataModel;

    /**
     * 构造方法
     * @param path 视图路径
     */
    public View (String path) {
        this.path = path;
        dataModel = new HashMap<String, Object>();
    }

    /**
     * 向视图添加数据
     * @param key 键值
     * @param value 值
     * @return 本对象（满足链式调用）
     */
    public View addDataToModel (String key, Object value) {
        dataModel.put(key, value);
        return this;
    }

    public String getPath () {
        return path;
    }

    public Map<String, Object> getDataModel () {
        return dataModel;
    }
}
