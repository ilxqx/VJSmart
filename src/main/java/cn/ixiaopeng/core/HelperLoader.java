package cn.ixiaopeng.core;

import cn.ixiaopeng.helper.BeanHelper;
import cn.ixiaopeng.helper.ClassHelper;
import cn.ixiaopeng.helper.ControllerHelper;
import cn.ixiaopeng.helper.IocHelper;
import cn.ixiaopeng.utils.ClassUtil;

/**
 * 加载Helper
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public final class HelperLoader {

    /**
     * 助手类初始化
     */
    public static void init () {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), true);
        }
    }
}
