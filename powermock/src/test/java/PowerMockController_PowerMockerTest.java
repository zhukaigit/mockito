import com.zk.common.model.PowerMockController;
import com.zk.common.model.PowerMockService1;
import com.zk.common.model.PowerMockService2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by zhukai on 2020/3/2
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ PowerMockService1.class })
public class PowerMockController_PowerMockerTest {

    @InjectMocks // 一般一个类只有一个这种注解，@Spy与@Mock修饰的字段注入到@InjectMocks修饰的字段对象中
    private PowerMockController powerMockController;

    @Spy    // 该注解修饰修饰的对象默认将调用真实代码，可对其中部分行为进行模拟，并注入到@InjectMocks修饰的对象中
    private PowerMockService1   powerMockService1 = new PowerMockService1();

    @Mock   // 该注解修饰修饰的对象将对所有行为进行模拟，否则默认返回，并注入到@InjectMocks修饰的对象中
    private PowerMockService2   powerMockService2;

    @Test
    public void test_say3() {
        when(powerMockService1.say()).thenReturn("1");
        when(powerMockService2.say()).thenReturn("2");
        Assert.assertEquals("12", powerMockController.say());
    }

    @Test
    public void test_say() {
        // 反射赋值，将powerMockService2注入到powerMockService1中
        initFieldForObject(PowerMockService1.class, powerMockService1, "powerMockService2", powerMockService2);
        when(powerMockService2.say()).thenReturn("2");
        Assert.assertEquals("p12x2", powerMockController.say());
    }

    @Test
    public void test_say2() {
        when(powerMockService2.say()).thenReturn("2");
        mockStatic(PowerMockService1.class);
        when(PowerMockService1.s()).thenReturn("u");
        Assert.assertEquals("p1p2u2", powerMockController.say());
    }

    public void initFieldForObject(Class targetClass, Object targetInstance, String fieldName, Object fieldValue) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(targetInstance, fieldValue);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(
                    String.format("%s没有该属性%s", targetInstance.getClass().getSimpleName(), fieldName));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("field access未设置成true");
        }
    }

}
