package cn.ixiaopeng.vj.smart.core;

/**
 * 封装表单参数
 * @author venus
 * @since 1.3.0
 * @version 1.0.0
 */
public class FormParam {
    // 表单名字
    private String fieldName;
    // 表单的值
    private Object fieldValue;

    public FormParam (String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
