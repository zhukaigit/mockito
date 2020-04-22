package com.zk.controller;

import com.zk.Enum.ColorEnum;
import com.zk.service.DemoService;
import com.zk.utils.AssertUtil;
import com.zk.utils.StringUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

@PrepareForTest({ DemoController.class })
public class DemoControllerTest extends BaseMockTest {
    @Mock
    DemoService    demoService;
    @InjectMocks
    DemoController demoController;

    // ============================= 1、非本类的方法模拟 - 开始 =============================

    /**
     * 模拟其他类 - 普通方法 - 有返回值
     */
    @Test
    public void testCall_other_method_common_return() throws Exception {
        PowerMockito.when(demoService.increase(Mockito.anyInt())).thenReturn(0);

        int result = demoController.call_other_method_common_return(0);
        Assert.assertEquals(0, result);
    }

    /**
     * 模拟其他类 - 普通void方法 - 抛异常
     */
    @Test
    public void testCall_other_method_common_void_exception() throws Exception {
        thrown.expect(Exception.class);
        PowerMockito.doThrow(new RuntimeException()).when(demoService).commonVoid(Mockito.anyInt());
        demoController.call_other_method_common_void();
    }

    /**
     * 模拟其他类 - 普通void方法 - 什么都不做
     */
    @Test
    public void testCall_other_method_common_void_1() throws Exception {
        // 由于demoService是mock对象（不能是spy对象），下面这行可以省略
        PowerMockito.doNothing().when(demoService).commonVoid(Mockito.anyInt());
        demoController.call_other_method_common_void();
    }

    /**
     * 模拟其他类 - 静态方法 - 有返回值
     */
    @Test
    @PrepareForTest({ StringUtil.class })
    public void testCall_other_method_static_return() throws Exception {
        PowerMockito.mockStatic(StringUtil.class);
        PowerMockito.when(StringUtil.toString(Mockito.any())).thenReturn("mockValue");
        String result = demoController.call_other_method_static_return(0);
        Assert.assertEquals("mockValue", result);
    }

    /**
     * 模拟其他类 - 静态方法 - 有返回值 - 抛出异常
     */
    @Test
    @PrepareForTest({ StringUtil.class })
    public void testCall_other_method_static_return_exception() throws Exception {
        PowerMockito.mockStatic(StringUtil.class);
        thrown.expect(RuntimeException.class);
        PowerMockito.when(StringUtil.toString(Mockito.any())).thenThrow(new RuntimeException());
        demoController.call_other_method_static_return(0);
    }

    /**
     * 模拟其他类 - 静态方法 - 无返回值
     */
    @Test
    @PrepareForTest({ AssertUtil.class })
    public void testCall_other_method_static_void() throws Exception {
        PowerMockito.mockStatic(AssertUtil.class);
        demoController.call_other_method_static_void(0);
    }

    /**
     * 模拟其他类 - 静态方法 - 无返回值 - 抛出异常
     */
    @Test
    @PrepareForTest({ AssertUtil.class })
    public void testCall_other_method_static_void_exception() throws Exception {
        PowerMockito.mockStatic(AssertUtil.class);
        thrown.expect(RuntimeException.class);
        PowerMockito.doThrow(new RuntimeException()).when(AssertUtil.class);
        demoController.call_other_method_static_void(0);
    }

    /**
     * 模拟其他类 - final方法 - 有返回值
     */
    @Test
    @PrepareForTest({ DemoService.class })
    public void testCall_other_method_final_return() throws Exception {
        PowerMockito.when(demoService.increaseFinal(Mockito.anyInt())).thenReturn(100);

        int result = demoController.call_other_method_final_return(10);
        Assert.assertEquals(100, result);
    }

    /**
     * 模拟其他类 - final方法 - 有返回值 - 抛出异常
     */
    @Test
    @PrepareForTest({ DemoService.class })
    public void testCall_other_method_final_return_exception() throws Exception {
        thrown.expect(RuntimeException.class);
        PowerMockito.when(demoService.increaseFinal(Mockito.anyInt())).thenThrow(new RuntimeException());
        demoController.call_other_method_final_return(0);
    }

    /**
     * 模拟其他类 - final方法 - 无返回值
     */
    @Test
    public void testCall_other_method_final_void() throws Exception {
        // 由于demoService是mock对象（不能是spy对象），下面这行可以省略
        PowerMockito.doNothing().when(demoService).finalVoid();
        demoController.call_other_method_final_void();
    }

    /**
     * PowerMockRule不支持模拟枚举常量的调用
     * 见{@link com.zk.controller.MockEnumCallTest#testCall_Enum}
     */
    @Test
    @Ignore
    @PrepareForTest({ ColorEnum.class })
    public void testCall_Enum() throws Exception {
        ColorEnum mock = PowerMockito.mock(ColorEnum.class);
        Whitebox.setInternalState(ColorEnum.class, "RED", mock);
        PowerMockito.when(mock.getDesc()).thenReturn("黑色");

        Assert.assertEquals("黑色", ColorEnum.RED.getDesc());
        Assert.assertEquals("蓝色", ColorEnum.BLUE.getDesc());

    }

    // ============================= 非本类的方法模拟 - 结束 =============================

    // ============================= 2、本类的方法模拟 - 开始 =============================
    // 注意：mock本类方法时，若不想进入到真实方法，则要使用doReturn().when()方式

    /**
     * 私有方法模拟 - 有返回值
     * 
     * @throws Exception
     */
    @Test
    public void testCall_self_private_return() throws Exception {
        // 注意：由于demoController是Spy对象，会去调用真实对象，如果使用when().thenReturn()方式，会先去调用真实方法，再返回模拟值
        // 如果使用doReturn().when()方式，则不会去调用真实对象，

        // doReturn在前，不会调用callPrivateHasReturn方法
        PowerMockito.doReturn("mockValue").when(demoController, "callPrivateHasReturn");
        // 下面则会去调用真实的callPrivateHasReturn方法
        // PowerMockito.when(demoController, "callPrivateHasReturn").thenReturn("mockValue");
        String result = demoController.call_self_private_return();
        Assert.assertEquals("mockValue", result);
    }

    /**
     * 私有方法模拟 - 无返回值
     * 
     * @throws Exception
     */
    @Test
    public void testCall_self_private_void() throws Exception {
        PowerMockito.doNothing().when(demoController, "callPrivateVoid");
        demoController.call_self_private_void();
    }

    // ============================= 2、本类的方法模拟 - 开始 =============================

}
