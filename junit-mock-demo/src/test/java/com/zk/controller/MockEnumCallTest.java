package com.zk.controller;

import com.zk.Enum.ColorEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
public class MockEnumCallTest {

    /**
     * 模拟枚举常量的调用
     *
     * 注意：在使用PowerMockRule这种方式模拟时，报错如下：
     * java.lang.IllegalArgumentException: Cannot subclass final class class com.zk.Enum.ColorEnum
     */
    @Test
    @PrepareForTest ({ColorEnum.class})
    public void testCall_Enum () throws Exception {
        ColorEnum mock = PowerMockito.mock(ColorEnum.class);
        Whitebox.setInternalState(ColorEnum.class, "RED", mock);
        PowerMockito.when(mock.getDesc()).thenReturn("黑色");

        Assert.assertEquals("黑色", ColorEnum.RED.getDesc());
        Assert.assertEquals("蓝色", ColorEnum.BLUE.getDesc());

    }

}
