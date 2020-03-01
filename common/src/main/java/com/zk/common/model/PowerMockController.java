package com.zk.common.model;

/**
 * Created by zhukai on 2020/3/2
 */
public class PowerMockController {

    private PowerMockService1 powerMockService1 = new PowerMockService1();

    private PowerMockService2 powerMockService2 = new PowerMockService2();

    /**
     * 期望powerMockService1真实调用，powerMockService2模拟调用
     * @return p1p2p2
     */
    public String say() {
        String s1 = powerMockService1.say();
        String s2 = powerMockService2.say();
        System.out.println(String.format("s1 = %s, s2 = %s", s1, s2));
        return s1 + s2;
    }

}
