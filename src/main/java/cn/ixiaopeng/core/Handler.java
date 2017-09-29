package cn.ixiaopeng.core;

import java.lang.reflect.Method;

/**
 * 封装请求处理是的控制器和方法信息
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public class Handler {
    // Controller 类
    private Class<?> controllerClass;

    // 方法
    private Method method;

    /**
     * 构造方法
     * @param controllerClass 控制器类
     * @param method 方法类
     */
    public Handler (Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getMethod() {
        return method;
    }
}
