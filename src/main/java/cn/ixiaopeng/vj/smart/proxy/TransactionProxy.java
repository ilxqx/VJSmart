package cn.ixiaopeng.vj.smart.proxy;

import cn.ixiaopeng.vj.smart.annotation.Transaction;
import cn.ixiaopeng.vj.smart.helper.DbHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 事务代理类
 * @author venus
 * @since 1.2.0
 * @version 1.0.0
 */
public class TransactionProxy implements Proxy {
    // 日志类
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);
    // 标志
    private static final ThreadLocal<Boolean> HOLDER_FLAG = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    /**
     * 执行代理链
     * @param proxyChain 代理链
     * @return 方法执行结果
     * @throws Throwable 抛出错误
     */
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        boolean flag = HOLDER_FLAG.get();
        Method method = proxyChain.getTargetMethod();
        if (!flag && method.isAnnotationPresent(Transaction.class)) {
            HOLDER_FLAG.set(true);
            try {
                DbHelper.beginTrans();
                LOGGER.debug("Begin transaction...");
                result = proxyChain.doProxyChain();
                DbHelper.commitTrans();
                LOGGER.debug("Commit transaction...");
            } catch (Throwable e) {
                DbHelper.rollbackTrans();
                LOGGER.debug("Rollback transaction...");
                throw e;
            } finally {
                HOLDER_FLAG.remove();
            }
        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
