package com.zk.controller;

import com.zk.service.DemoService;
import com.zk.utils.AssertUtil;
import com.zk.utils.StringUtil;

public class DemoController {

    private DemoService demoService;

    /**
     * 调用其他类 - 普通方法 - 有返回值
     */
    public int call_other_method_common_return(int num) {
        System.out.println("DemoController.call_other_method_common_return 被调用了...");
        int increase = demoService.increase(num);
        System.out.println("demoService.increase(num)返回结果：" + increase);
        return increase;
    }

    /**
     * 调用其他类 - 普通方法 - 无返回值
     */
    public void call_other_method_common_void() {
        System.out.println("DemoController.call_other_method_common_void 被调用了...");
        demoService.commonVoid(10);
        System.out.println("demoService.commonVoid()调用完成");
    }

    /**
     * 调用其他类 - static方法 - 有返回值
     */
    public String call_other_method_static_return(int id) {
        System.out.println("DemoController.call_other_method_static_void 被调用了...");
        String idStr = StringUtil.toString(id);
        System.out.println(" StringUtil.toString(id)返回结果：" + idStr);
        return idStr;
    }

    /**
     * 调用其他类 - static方法 - 无返回值
     */
    public void call_other_method_static_void(int id) {
        System.out.println("DemoController.call_other_method_static_void 被调用了...");
        AssertUtil.assertTrue(id > 0, "id不能小于0");
        System.out.println("AssertUtil.assertTrue调用完成");
    }

    /**
     * 调用其他类 - final方法 - 有返回值
     */
    public int call_other_method_final_return(int num) {
        System.out.println("DemoController.call_other_method_static_void 被调用了...");
        int increase = demoService.increaseFinal(num);
        System.out.println("demoService.increaseFinal(num)返回结果：" + increase);
        return increase;
    }

    /**
     * 调用其他类 - final方法 - 无返回值
     */
    public void call_other_method_final_void() {
        System.out.println("DemoController.call_other_method_static_void 被调用了...");
        demoService.finalVoid();
        System.out.println("demoService.finalVoid()调用完成");
    }

    /**
     * 调用本类 - private方法 - 有返回值
     */
    public String call_self_private_return() {
        System.out.println("DemoController.call_self_private_return 被调用了...");
        String s = callPrivateHasReturn();
        System.out.println("callPrivateHasReturn()返回结果：" + s);
        return s;
    }

    /**
     * 调用本类 - private方法 - 无返回值
     */
    public void call_self_private_void() {
        System.out.println("DemoController.call_self_private_void 被调用了...");
        callPrivateVoid();
        System.out.println("callPrivateVoid()调用完成");
    }

    private String callPrivateHasReturn() {
        System.out.println("UserController.callPrivateHasReturn 私有方法被调用了...");
        return "2000-1-1";
    }

    private void callPrivateVoid() {
        System.out.println("UserController.callPrivateVoid 私有方法被调用了...");
    }

}
