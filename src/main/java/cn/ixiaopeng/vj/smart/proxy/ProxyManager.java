package cn.ixiaopeng.vj.smart.proxy;

import cn.ixiaopeng.vj.smart.utils.CastUtil;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理管理类
 * @author venus
 * @since 1.1.0
 * @version 1.0.0
 */
public final class ProxyManager {

    /**
     * 创建代理
     * @param targetClass 目标代理类
     * @param proxyList 执行的代理List
     * @param <T> 类型
     * @return 返回代理后的对象
     */
    public static <T> T createProxy (final Class<?> targetClass, final List<Proxy> proxyList) {
        return CastUtil.cast(Enhancer.create(targetClass, new MethodInterceptor() {
            /**
             * 拦截方法
             * @param o 目标对象
             * @param method 目标方法
             * @param objects 方法参数
             * @param methodProxy 方法代理
             * @return 方法执行结果
             * @throws Throwable 抛出异常
             */
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass, o, method, methodProxy, objects, proxyList).doProxyChain();
            }
        }));
    }
}
