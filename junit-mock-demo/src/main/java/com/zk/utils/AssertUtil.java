package com.zk.utils;

public class AssertUtil {

    public static void assertTrue (boolean condition, String errMsg) {
        System.out.println("AssertUtil.assertTrue 被调用了...");
        if (!condition) {
            throw new RuntimeException(errMsg);
        }
    }

}
