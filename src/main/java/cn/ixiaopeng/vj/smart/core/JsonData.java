package cn.ixiaopeng.vj.smart.core;

/**
 * 返回数据对象
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public class JsonData {
    // 数据模型
    private Object dataModel;

    /**
     * 构造方法
     * @param dataModel 数据模型
     */
    public JsonData (Object dataModel) {
        this.dataModel = dataModel;
    }

    public Object getDataModel() {
        return dataModel;
    }
}
