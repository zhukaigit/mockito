package com.zk.utils;

public class StringUtil {

    public static String toString (Object obj) {
        System.out.println("StringUtil.toString 被调用了...");
        return String.valueOf(obj);
    }
}
