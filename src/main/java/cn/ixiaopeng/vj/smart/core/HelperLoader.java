package cn.ixiaopeng.vj.smart.core;

import cn.ixiaopeng.vj.smart.helper.*;
import cn.ixiaopeng.vj.smart.utils.ClassUtil;

/**
 * 加载Helper
 * @author venus
 * @since 1.0.0
 * @version 1.1.0
 */
public final class HelperLoader {

    /**
     * 助手类初始化
     */
    public static void init () {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), true);
        }
    }
}
