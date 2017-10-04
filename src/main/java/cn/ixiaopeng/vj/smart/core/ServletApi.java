package cn.ixiaopeng.vj.smart.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ServletHelper助手类
 * @author venus
 * @since 1.4.1
 * @version 1.0.0
 */
public final class ServletApi {
    // 每个线程只能拥有一个ServletHelper实例
    private static final ThreadLocal<ServletApi> SERVLET_HELPER = new ThreadLocal<ServletApi>();

    // 请求对象
    private HttpServletRequest request;
    // 响应对象
    private HttpServletResponse response;

    /**
     * 私有构造方法
     * @param request 请求对象
     * @param response 响应对象
     */
    private ServletApi(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * 静态初始化方法
     * @param request 请求对象
     * @param response 响应对象
     */
    private static void init (HttpServletRequest request, HttpServletResponse response) {
        SERVLET_HELPER.set(new ServletApi(request, response));
    }

    /**
     * 销毁方法
     */
    private static void destroy () {
        SERVLET_HELPER.remove();
    }

    /**
     * 获取线程对应请求对象
     * @return 请求对象
     */
    public static HttpServletRequest getRequest () {
        return SERVLET_HELPER.get().request;
    }

    /**
     * 获取线程对应响应对象
     * @return 响应对象
     */
    public static HttpServletResponse getResponse () {
        return SERVLET_HELPER.get().response;
    }
}
