package cn.ixiaopeng.vj.smart.helper;

import cn.ixiaopeng.vj.smart.core.Handler;
import cn.ixiaopeng.vj.smart.core.Request;
import cn.ixiaopeng.vj.smart.utils.ArrayUtil;
import cn.ixiaopeng.vj.smart.utils.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class ControllerHelper {
    // 用于封装请求和处理之间的映射关系Map
    private static final Map<Request, Handler> REQUEST_HANDLER_MAP = new HashMap<Request, Handler>();

    static {
        // 获取所有的Controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            // 遍历这些类
            for (Class<?> controllerClass : controllerClassSet) {
                // 获取Controller中定义的所有方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {
                    // 遍历这些方法
                    for (Method method : methods) {
                        // 判断当前方法是否带有Method注解
                        if (method.isAnnotationPresent(cn.ixiaopeng.vj.smart.annotation.Method.class)) {
                            // 从Method注解中获取URL映射规则
                            cn.ixiaopeng.vj.smart.annotation.Method annotationMethod = method.getAnnotation(cn.ixiaopeng.vj.smart.annotation.Method.class);
                            String mapping = annotationMethod.value();
                            // 验证URL规则
                            if (mapping.matches("\\w{3,6}:/\\w*")) {
                                String[] strArr = mapping.split(":");
                                if (ArrayUtil.isNotEmpty(strArr) && strArr.length == 2) {
                                    // 获取请求方法与请求路径
                                    String requestMethod = strArr[0];
                                    String requestPath = strArr[1];
                                    Request request = new Request(requestMethod, requestPath);
                                    Handler handler = new Handler(controllerClass, method);
                                    // 添加到映射Map中
                                    REQUEST_HANDLER_MAP.put(request, handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据请求获取Handler对象
     * @param requestMethod 请求方法
     * @param requestPath 请求路径
     * @return Handler对象
     */
    public static Handler getHandler (String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return REQUEST_HANDLER_MAP.get(request);
    }
}
