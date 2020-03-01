package com.zk.common.model;

/**
 * Created by zhukai on 2020/3/2
 */
public class PowerMockService1 {

    private PowerMockService2 powerMockService2 = new PowerMockService2();

    public String say() {
        return "p1" + powerMockService2.say() + s();
    }

    public static String s() {
        return "x";
    }
}
