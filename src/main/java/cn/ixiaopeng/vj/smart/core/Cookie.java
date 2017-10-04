package cn.ixiaopeng.vj.smart.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cookie
 * @author venus
 * @since 1.4.0
 * @version 1.0.0
 */
public class Cookie {
    // Cookie
    private static final ThreadLocal<List<javax.servlet.http.Cookie>> COOKIE = new ThreadLocal<List<javax.servlet.http.Cookie>>();
    private static final ThreadLocal<HttpServletResponse> RESPONSE = new ThreadLocal<HttpServletResponse>();

    //初始化Cookie
    private static void init (HttpServletRequest request, HttpServletResponse response) {
        List<javax.servlet.http.Cookie> cookieList = new ArrayList<javax.servlet.http.Cookie>();
        cookieList.addAll(Arrays.asList(request.getCookies()));
        COOKIE.set(cookieList);
        RESPONSE.set(response);
    }
    // 销毁CookieList
    private static void destroy () {
        COOKIE.remove();
        RESPONSE.remove();
    }

    /**
     * 获取CookieList
     * @return List
     */
    private static List<javax.servlet.http.Cookie> getCookie () {
        return COOKIE.get();
    }

    /**
     * 获取Response对象
     * @return Response
     */
    private static HttpServletResponse getResponse () {
        return RESPONSE.get();
    }

    /**
     * 添加Cookie
     * @param key 键值
     * @param value 值
     * @param maxAge 最大生存时间
     * @param path 路径
     * @param domain 域
     */
    public static void set (String key, String value, int maxAge, String path, String domain) {
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(key, value);
        cookie.setMaxAge(maxAge > 0 ? maxAge : -1);
        if (path != null) {
            cookie.setPath(path);
        }
        if (domain != null) {
            cookie.setDomain(domain);
        }
        getCookie().add(cookie);
        getResponse().addCookie(cookie);
    }

    public static void set (String key, String value, int maxAge, String path) {
        set(key, value, maxAge, path, null);
    }

    public static void set (String key, String value, int maxAge) {
        set(key, value, maxAge, null);
    }

    public static void set (String key, String value) {
        set(key, value, -1);
    }

    /**
     * 获取Cookie对象
     * @param key 键值
     * @return Cookie对象
     */
    private static javax.servlet.http.Cookie getCookieObj (String key) {
        List<javax.servlet.http.Cookie> cookieList = getCookie();
        for (javax.servlet.http.Cookie cookie : cookieList) {
            if (cookie.getName().equals(key)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * 获取Cookie值
     * @param key 键值
     * @return String
     */
    public static String get (String key) {
        javax.servlet.http.Cookie cookie = getCookieObj(key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 删除Cookie
     * @param key 键值
     */
    public static void remove (String key) {
        javax.servlet.http.Cookie cookie = getCookieObj(key);
        if (cookie != null) {
            cookie.setPath("/");
            cookie.setMaxAge(0);
        }
    }

    /**
     * 删除整个Cookie
     */
    public static void invalidate () {
        List<javax.servlet.http.Cookie> cookieList = getCookie();
        for (javax.servlet.http.Cookie cookie : cookieList) {
            remove(cookie.getName());
        }
    }
}
