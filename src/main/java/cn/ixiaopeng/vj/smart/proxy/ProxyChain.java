package cn.ixiaopeng.vj.smart.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理链
 * @author venus
 * @since 1.1.0
 * @version 1.0.0
 */
public class ProxyChain {
    // 目标类
    private final Class<?> targetClass;
    // 目标对象
    private final Object targetObject;
    // 目标方法
    private final Method targetMethod;
    // 目标方法的代理
    private final MethodProxy methodProxy;
    // 目标方法执行所需参数
    private final Object[] targetMethodParams;
    // 代理的List
    private List<Proxy> proxyList = new ArrayList<Proxy>();
    // 执行代理链的当前执行计数
    private int proxyIndex = 0;

    /**
     * 构造方法
     * @param targetClass 目标类
     * @param targetObject 目标对象
     * @param targetMethod 目标方法
     * @param methodProxy 方法代理
     * @param targetMethodParams 目标方法参数
     * @param proxyList 代理List
     */
    public ProxyChain (Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] targetMethodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.targetMethodParams = targetMethodParams;
        this.proxyList = proxyList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getTargetMethodParams() {
        return targetMethodParams;
    }

    /**
     * 执行代理链（链式执行，返回则相反）
     * @return 方法执行结果
     * @throws Throwable 抛出异常（交由上级处理）
     */
    public Object doProxyChain () throws Throwable {
        Object result;
        if (proxyIndex < proxyList.size()) {
            result = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            result = methodProxy.invokeSuper(targetObject, targetMethodParams);
        }
        return result;
    }
}
