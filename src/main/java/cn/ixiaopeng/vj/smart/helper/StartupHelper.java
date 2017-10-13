package cn.ixiaopeng.vj.smart.helper;

import cn.ixiaopeng.vj.smart.core.*;
import cn.ixiaopeng.vj.smart.utils.CastUtil;
import cn.ixiaopeng.vj.smart.utils.JsonUtil;
import cn.ixiaopeng.vj.smart.utils.ReflectionUtil;
import cn.ixiaopeng.vj.smart.utils.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 整个框架启动助手
 * @author venus
 * @since 1.3.0
 * @version 1.2.0
 */
public class StartupHelper {
    /**
     * 初始化框架
     * @param servletConfig ServletConfig配置对象
     */
    public static void init (ServletConfig servletConfig) {
        // 初始化相关Helper
        HelperLoader.init();
        // 获取ServletContext对象用于注册Servlet
        ServletContext servletContext = servletConfig.getServletContext();
        // 注册处理JSP的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        // 注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
        // 初始化上传助手类
        UploaderHelper.init(servletContext);
    }

    /**
     * 请求调度
     * @param request 请求对象
     * @param response 响应对象
     * @throws IOException 抛出IO异常
     * @throws ServletException 抛出Servlet异常
     */
    public static void dispatcher (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, NoSuchMethodException {
        // 初始化Session
        ReflectionUtil.invokeMethod(null, Session.class.getDeclaredMethod("init", HttpSession.class), request.getSession());
        // 初始化Cookie
        ReflectionUtil.invokeMethod(null, Cookie.class.getDeclaredMethod("init", HttpServletRequest.class, HttpServletResponse.class), request, response);
        // 初始化ServletApi
        ReflectionUtil.invokeMethod(null, ServletApi.class.getDeclaredMethod("init", HttpServletRequest.class, HttpServletResponse.class), request, response);
        try {
            // 获取请求方法与请求路径
            String requestMethod = request.getMethod().toLowerCase();
            String requestPath = request.getPathInfo();
            // 跳过favicon.icon
            if (requestPath != null && requestPath.equals("/favicon.ico")) {
                return;
            }
            // 获取Method处理器
            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if (handler != null) {
                // 获取Controller类及其实例
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerInstance = BeanHelper.getBean(controllerClass);
                // 创建请求参数
                Params params;
                if (UploaderHelper.isMultipart(request)) {
                    params = UploaderHelper.createParams(request);
                } else {
                    params = RequestHelper.createParams(request);
                }
                // 调用Method方法
                Method method = handler.getMethod();
                Class<?>[] types = method.getParameterTypes();
                Object[] arguments = new Object[method.getParameterCount()];
                int index = 0;
                // 方法参数依赖注入
                for (Class<?> type : types) {
                    if (type.equals(Params.class)) {
                        arguments[index++] = params;
                    } else if (type.equals(HttpServletRequest.class)) {
                        arguments[index++] = request;
                    } else if (type.equals(HttpServletResponse.class)) {
                        arguments[index++] = response;
                    } else {
                        arguments[index++] = null;
                    }
                }
                // 方法调用
                Object result = ReflectionUtil.invokeMethod(controllerInstance, method, arguments);
                // 处理结果
                if (response.isCommitted()) {
                    return;
                }
                if (result instanceof View) {
                    handleViewResult(CastUtil.cast(result), request, response);
                } else if (result instanceof JsonData) {
                    handleJsonDataResult(CastUtil.cast(result), request, response);
                }
            } else {
            	response.setContentType("text/html;charset=utf8");
                response.setCharacterEncoding("UTF-8");
                PrintWriter printWriter = response.getWriter();
                printWriter.write("<h1>404 Not found!</h1>");
                printWriter.flush();
                printWriter.close();
            }
        } finally {
            ReflectionUtil.invokeMethod(null, Session.class.getDeclaredMethod("destroy"));
            ReflectionUtil.invokeMethod(null, Cookie.class.getDeclaredMethod("destroy"));
            ReflectionUtil.invokeMethod(null, ServletApi.class.getDeclaredMethod("destroy"));
        }
    }

    /**
     * 处理视图返回
     * @param view 视图对象
     * @param request 请求对象
     * @param response 响应对象
     * @throws IOException 抛出IO异常
     * @throws ServletException 抛出Servlet异常
     */
    private static void handleViewResult (View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 返回JSP页面
        String path = view.getPath();
        if (StringUtil.isNotEmpty(path)) {
            if (path.startsWith("/")) {
                // 重定向
                response.sendRedirect(request.getContextPath() + path);
            } else {
                // 视图展示
                Map<String, Object> dataModel = view.getDataModel();
                for (Map.Entry<String, Object> entry : dataModel.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
            }
        }
    }

    /**
     * 处理Json返回
     * @param jsonData JsonData对象
     * @param request 请求对象
     * @param response 响应对象
     * @throws IOException 抛出IO异常
     */
    private static void handleJsonDataResult (JsonData jsonData, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 返回JSON数据
        Object dataModel = jsonData.getDataModel();
        if (dataModel != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();
            String json = JsonUtil.toJson(dataModel);
            printWriter.write(json);
            printWriter.flush();
            printWriter.close();
        }
    }
}
