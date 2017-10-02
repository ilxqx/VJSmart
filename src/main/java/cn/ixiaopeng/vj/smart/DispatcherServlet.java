package cn.ixiaopeng.vj.smart;

import cn.ixiaopeng.vj.smart.core.*;
import cn.ixiaopeng.vj.smart.helper.BeanHelper;
import cn.ixiaopeng.vj.smart.helper.ConfigHelper;
import cn.ixiaopeng.vj.smart.helper.ControllerHelper;
import cn.ixiaopeng.vj.smart.utils.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求转发
 * @author venus
 * @since 1.0.0
 * @version 1.1.0
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        // 初始化相关Helper
        HelperLoader.init();
        // 获取ServletContext对象用于注册Servlet
        ServletContext servletContext = getServletConfig().getServletContext();
        // 注册处理JSP的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        // 注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求方法与请求路径
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();
        // 获取Method处理器
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            // 获取Controller类及其实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerInstance = BeanHelper.getBean(controllerClass);
            // 创建请求参数对象
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            Enumeration<String> paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = req.getParameter(paramName);
                paramsMap.put(paramName, paramValue);
            }

            String urlStr = CodecUtil.decodeUrl(StreamUtil.getString(req.getInputStream()));
            if (StringUtil.isNotEmpty(urlStr)) {
                String[] params = urlStr.split("&");
                if (ArrayUtil.isNotEmpty(params)) {
                    for (String param : params) {
                        String[] arr = param.split("=");
                        if (ArrayUtil.isNotEmpty(arr) && arr.length == 2) {
                            paramsMap.put(arr[0], arr[1]);
                        }
                    }
                }
            }

            Params params = new Params(paramsMap);
            // 调用Method方法
            Method method = handler.getMethod();
            Class<?>[] types = method.getParameterTypes();
            Object[] arguments = new Object[method.getParameterCount()];
            int index = 0;
            // 方法参数依赖注入
            for (Class<?> type : types) {
                if (type.equals(Params.class)) {
                    arguments[index++] = params;
                } else {
                    arguments[index++] = null;
                }
            }
            Object result = ReflectionUtil.invokeMethod(controllerInstance, method, arguments);
            // 处理方法返回值
            if (result instanceof View) {
                // 返回JSP页面
                View view = (View) result;
                String path = view.getPath();
                if (StringUtil.isNotEmpty(path)) {
                    if (path.startsWith("/")) {
                        // 重定向
                        resp.sendRedirect(req.getContextPath() + path);
                    } else {
                        // 视图展示
                        Map<String, Object> dataModel = view.getDataModel();
                        for (Map.Entry<String, Object> entry : dataModel.entrySet()) {
                            req.setAttribute(entry.getKey(), entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
                    }
                }
            } else if (result instanceof JsonData) {
                // 返回JSON数据
                JsonData jsonData = (JsonData) result;
                Object dataModel = jsonData.getDataModel();
                if (dataModel != null) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter printWriter = resp.getWriter();
                    String json = JsonUtil.toJson(dataModel);
                    printWriter.write(json);
                    printWriter.flush();
                    printWriter.close();
                }
            }
        }
    }
}
