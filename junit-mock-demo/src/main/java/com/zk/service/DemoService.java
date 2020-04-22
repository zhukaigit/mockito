package com.zk.service;

public class DemoService {

    public final void updateName (Long id) {
        System.out.println("UserService updateName 被调用了...");
    }

    public int increase (int num) {
        System.out.println("DemoService.increase 被调用了...");
        return num + 1;
    }

    public void commonVoid (int id) {
        System.out.println("DemoService.commonVoid 被调用了...");
    }

    public final int increaseFinal (int num) {
        System.out.println("DemoService.increase 被调用了...");
        return num + 1;
    }

    public final void finalVoid () {
        System.out.println("DemoService.finalVoid 被调用了...");

    }

}
