package cn.ixiaopeng.vj.smart.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 封装请求信息
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public class Request {
    // 请求方法
    private String requestMethod;

    // 请求路径
    private String requestPath;

    /**
     * 构造方法
     * @param requestMethod 请求方法
     * @param requestPath 请求路径
     */
    public Request (String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
