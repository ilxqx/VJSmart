package cn.ixiaopeng.vj.smart.proxy;

import cn.ixiaopeng.vj.smart.core.ServletApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 切面代理类
 * @author venus
 * @since 1.1.0
 * @version 1.0.0
 */
public abstract class AspectProxy implements Proxy {
    // 日志类
    private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    /**
     * 执行代理
     * @param proxyChain 代理链
     * @return 方法执行结果
     * @throws Throwable 抛出异常
     */
    @Override
    public final Object doProxy (ProxyChain proxyChain) throws Throwable {
        Object result = null;
        HttpServletResponse response = ServletApi.getResponse();
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getTargetMethodParams();
        // 开始代理
        begin();
        try {
            if (intercept(cls, method, params)) {
                // 方法执行前代理
                if (!response.isCommitted()) {
                    before(cls, method, params);
                }
                if (!response.isCommitted()) {
                    result = proxyChain.doProxyChain();
                }

                // 方法执行后代理
                if (!response.isCommitted()) {
                    after(cls, method, params, result);
                }
            } else {
                // 不拦截的方法不需要代理
                result = proxyChain.doProxyChain();
            }
        } catch (Throwable throwable) {
            LOGGER.error("Proxy failure", throwable);
            // 出错后的代理
            error(cls, method, params, throwable);
        } finally {
            // 结束代理
            end();
        }
        return result;
    }

    /**
     * 拦截处理（返回true表示拦截，也就是代理，反之亦然），默认所有方法都拦截
     * @param cls 目标类
     * @param method 目标对象方法
     * @param params 方法参数
     * @return 是否拦截
     * @throws Throwable 抛出异常
     */
    public boolean intercept (Class<?> cls, Method method, Object[] params) throws Throwable {
        return true;
    }

    public void begin () {

    }

    public void end () {

    }

    public void before (Class<?> cls, Method method, Object[] params) throws Throwable {

    }

    public void after (Class<?> cls, Method method, Object[] params, Object result) throws Throwable {

    }

    public void error (Class<?> cls, Method method, Object[] params, Throwable throwable) {

    }
}
