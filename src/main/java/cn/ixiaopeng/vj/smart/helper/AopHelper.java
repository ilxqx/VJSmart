package cn.ixiaopeng.vj.smart.helper;

import cn.ixiaopeng.vj.smart.annotation.Aspect;
import cn.ixiaopeng.vj.smart.proxy.AspectProxy;
import cn.ixiaopeng.vj.smart.proxy.Proxy;
import cn.ixiaopeng.vj.smart.proxy.ProxyManager;
import cn.ixiaopeng.vj.smart.proxy.TransactionProxy;
import cn.ixiaopeng.vj.smart.utils.CastUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 方法拦截（AOP）助手类
 * @author venus
 * @since 1.1.0
 * @version 1.1.0
 */
public final class AopHelper {
    // 日志类
    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            Map<Class<?>, List<Proxy>> targetProxyMap = getTargetProxyMap(getProxyMap());
            for (Map.Entry<Class<?>, List<Proxy>> entry : targetProxyMap.entrySet()) {
                Class<?> targetClass = entry.getKey();
                Object proxy = ProxyManager.createProxy(targetClass, entry.getValue());
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            LOGGER.error("Aop failure", e);
        }
    }

    /**
     * 根据Aspect注解的参数目标注解来获取所有的目标类（带有目标注解的类）
     * @param aspect 切面注解
     * @return 类集合
     * @throws Exception 抛出异常
     */
    private static Set<Class<?>> getTargetClassSet (Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if (!annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     * 获取代理类与目标类的映射关系Map
     * @return Map
     * @throws Exception 抛出异常
     */
    private static Map<Class<?>, Set<Class<?>>> getProxyMap () throws Exception {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
        addAspectProxy(proxyMap);
        addTransProxy(proxyMap);
        return proxyMap;
    }

    /**
     * 添加切面代理
     * @param proxyMap 代理Map
     * @throws Exception 抛出异常
     */
    private static void addAspectProxy (Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = getTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
    }

    /**
     * 添加事务代理
     * @param proxyMap 代理Map
     */
    private static void addTransProxy (Map<Class<?>, Set<Class<?>>> proxyMap) {
        Set<Class<?>> serviceClassSet = ClassHelper.getServiceClassSet();
        proxyMap.put(TransactionProxy.class, serviceClassSet);
    }

    /**
     * 获取目标对象和代理列表之间的映射
     * @param proxyMap 代理和目标类的映射
     * @return Map
     * @throws Exception 抛出异常
     */
    private static Map<Class<?>, List<Proxy>> getTargetProxyMap (Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetProxyMap = new HashMap<Class<?>, List<Proxy>>();
        for (Map.Entry<Class<?>, Set<Class<?>>> entry : proxyMap.entrySet()) {
            Class<?> proxyClass = entry.getKey();
            Set<Class<?>> targetClassSet = entry.getValue();
            for (Class<?> targetClass : targetClassSet) {
                Proxy proxy = CastUtil.cast(proxyClass.newInstance());
                if (targetProxyMap.containsKey(targetClass)) {
                    targetProxyMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetProxyMap.put(targetClass, proxyList);
                }
            }
        }
        return targetProxyMap;
    }
}
