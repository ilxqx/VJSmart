package cn.ixiaopeng.vj.smart.core;

import cn.ixiaopeng.vj.smart.utils.CastUtil;

import javax.servlet.http.HttpSession;

/**
 * Session操作
 * @author venus
 * @since 1.4.0
 * @version 1.0.0
 */
public class Session {
    // Session
    private static final ThreadLocal<HttpSession> SESSION = new ThreadLocal<HttpSession>();

    /**
     * 初始化
     * @param httpSession Session对象
     */
    private static void init (HttpSession httpSession) {
        SESSION.set(httpSession);
    }

    /**
     * 销毁
     */
    private static void destroy () {
        SESSION.remove();
    }

    /**
     * 获取HttpSession对象
     * @return HttpSession
     */
    private static HttpSession getSession () {
        return SESSION.get();
    }

    /**
     * 设置Session值
     * @param key 键值
     * @param value 值
     */
    public static void set (String key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * 获取Session值
     * @param key 键值
     * @param <T> 返回类型
     * @return 值
     */
    public static <T> T get (String key) {
        return CastUtil.cast(getSession().getAttribute(key));
    }

    /**
     * 移除某个值
     * @param key 键值
     */
    public static void remove (String key) {
        getSession().removeAttribute(key);
    }

    /**
     * 销毁整个Session
     */
    public static void invalidate () {
        getSession().invalidate();
    }
}
