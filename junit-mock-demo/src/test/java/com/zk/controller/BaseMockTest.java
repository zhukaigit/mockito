package com.zk.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 所有PowerMock的基类
 */
public abstract class BaseMockTest {

    @Rule
    public PowerMockRule     powerMockRule = new PowerMockRule();
    @Rule
    public ExpectedException thrown        = ExpectedException.none();

    /**
     * 描述：
     * 1、将@InjectMocks修饰的属性对象用{@Code PowerMockito.spy()}构造出来并赋值，目的是可以模拟本类的方法进行mock。
     * 2、将@Mock修饰的属性对象用{@Code PowerMockito.mock()}构造出来并赋值，目的是mock静态方法、私有方法、final方法。
     * 3、将所有的mock对象通过反射机制注入到spy对象中
     * 注意：目标测试类@Mock修饰属性名称最好与目标类属性名称一致，若不一致且目标类的多个FieldType相同，则无法注入
     */
    @Before
    public void setUp() {
        try {
            /* 给目标测试类的所有属性创建spy和mock对象，并赋值 */
            Field[] fields = this.getClass().getDeclaredFields();// 获取测试类下的所有Field
            Object target = null;// 目标测试类实例
            Class targetType = null;// 目标测试类Class类型
            Map<FieldInfo, MockObjInfo> mockObjInfoMap = new HashMap<>();// 存放所有@Mock修饰的属性名称及其对应的MockObjInfo信息
            int injectMockCount = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (field.isAnnotationPresent(InjectMocks.class)) {
                    Object instance = null;
                    try {
                        Constructor<?> constructor = fieldType.getConstructor();
                        constructor.setAccessible(true);
                        instance = constructor.newInstance();
                        injectMockCount++;
                    } catch (Exception e) {
                        throw new RuntimeException(String.format("%s没有无参构造函数", fieldType.getName()));
                    }
                    target = PowerMockito.spy(instance);
                    targetType = fieldType;
                    field.set(this, target);
                } else if (field.isAnnotationPresent(Mock.class)) {
                    Object mock = PowerMockito.mock(fieldType);
                    mockObjInfoMap.put(new FieldInfo(field.getName(), fieldType), new MockObjInfo(mock, fieldType));
                    field.set(this, mock);
                }
            }

            if (injectMockCount == 0) {
                System.err.println("提示：没有@InjectMocks注释的对象");
                return;
            }

            if (injectMockCount > 1) {
                throw new RuntimeException("@InjectMocks注释的对象只能有一个");
            }

            if (target == null || mockObjInfoMap.isEmpty()) {
                return;
            }

            /* 目标测试类的属性注入 */
            Field[] declaredFields = targetType.getDeclaredFields();
            for (Field targetField : declaredFields) {
                targetField.setAccessible(true);

                // 1、根据字段名称和字段类型完全一致类匹配
                Optional<FieldInfo> optional = mockObjInfoMap.keySet().stream()
                        .filter(fieldInfo -> targetField.getName().equals(fieldInfo.getFieldName())
                                && targetField.getType().isAssignableFrom(fieldInfo.getFieldType()))
                        .findFirst();
                if (optional.isPresent()) {
                    MockObjInfo mockObjInfo = mockObjInfoMap.get(optional.get());
                    if (mockObjInfo != null && mockObjInfo.getMockInstance() != null) {
                        targetField.set(target, mockObjInfo.getMockInstance());
                        continue;
                    }
                }

                // 2、根据字段类型类匹配
                List<FieldInfo> collect = mockObjInfoMap.keySet().stream()
                        .filter(fieldInfo -> targetField.getType() == fieldInfo.getFieldType())
                        .collect(Collectors.toList());
                if (collect != null && collect.size() == 1) {
                    MockObjInfo mockObjInfo = mockObjInfoMap.get(collect.get(0));
                    if (mockObjInfo != null && mockObjInfo.getMockInstance() != null) {
                        targetField.set(target, mockObjInfo.getMockInstance());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    @AllArgsConstructor
    private static class MockObjInfo {
        // 通过PowerMock.mock()创造出来的对象
        private Object mockInstance;
        // 属性类型
        private Class  fieldType;
    }

    @Data
    @AllArgsConstructor
    private static class FieldInfo {
        private String fieldName;
        private Class  fieldType;
    }

}
