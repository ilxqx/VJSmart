package cn.ixiaopeng.test;

import org.junit.Test;

public class ClassUtilTest {
    @Test
    public void getClassSetTest () {
        try {
            throw new RuntimeException("Error");
        } catch (RuntimeException e) {
            System.out.println("Encounter a error");
        }
    }
}
