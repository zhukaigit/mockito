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
import java.util.Map;

/**
 * 所有PowerMock的基类
 */
public class BaseMockTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            Object target = null;
            Class targetType = null;
            Map<String, MockObjInfo> mockObjInfoMap = new HashMap<>();
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
                    mockObjInfoMap.put(field.getName(), new MockObjInfo(mock, fieldType));
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

            // target赋值
            if (target == null || mockObjInfoMap.isEmpty()) {
                return;
            }

            Field[] declaredFields = targetType.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                MockObjInfo mockObjInfo = mockObjInfoMap.get(declaredField.getName());
                if (mockObjInfo != null && mockObjInfo.getMockInstance() != null
                        && declaredField.getType().isAssignableFrom(mockObjInfo.getFieldType())) {
                    declaredField.set(target, mockObjInfo.getMockInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Data
    @AllArgsConstructor
    private static class MockObjInfo {
        private Object mockInstance;
        private Class fieldType;
    }
}
