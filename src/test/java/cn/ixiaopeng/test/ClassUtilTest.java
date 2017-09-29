package cn.ixiaopeng.test;

import cn.ixiaopeng.utils.ClassUtil;
import org.junit.Test;

import java.util.Set;

public class ClassUtilTest {
    @Test
    public void getClassSetTest () {
        Set<Class<?>> classSet = ClassUtil.getClassSet("cn.ixiaopeng");
        for (Class<?> cls : classSet) {
            System.out.println(cls.getName());
            System.out.println(cls.getSimpleName());
        }
    }
}
