package cn.ixiaopeng.vj.smart;

import cn.ixiaopeng.vj.smart.helper.StartupHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.ixiaopeng.vj.smart.helper.StartupHelper.dispatcher;

/**
 * 请求转发
 * @author venus
 * @since 1.0.0
 * @version 1.2.0
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        StartupHelper.init(getServletConfig());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            dispatcher(req, resp);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
