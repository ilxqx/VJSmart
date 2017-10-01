package cn.ixiaopeng.proxy;

/**
 * 实现代理的接口
 * @author venus
 * @since 1.1.0
 * @version 1.0.0
 */
public interface Proxy {
    /**
     * 执行链式代理
     * @param proxyChain 代理链
     * @return 方法执行结果
     * @throws Throwable 抛出异常
     */
    Object doProxy (ProxyChain proxyChain) throws Throwable;
}
