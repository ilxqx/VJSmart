package cn.ixiaopeng.vj.smart.core;

/**
 * 工厂助手类
 * @author venus
 * @since 1.2.0
 * @version 1.0.0
 */
public final class Helper {
    /**
     * Json返回
     * @param object 待序列化对象
     * @return JsonData对象
     */
    public static JsonData json (Object object) {
        return new JsonData(object);
    }

    /**
     * View 返回
     * @param path 视图文件路径
     * @return View对象
     */
    public static View view (String path) {
        return new View(path);
    }
}
