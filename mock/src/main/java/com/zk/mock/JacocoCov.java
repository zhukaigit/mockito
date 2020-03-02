package com.zk.mock;

/**
 * jacoco覆盖率测试示例
 */
public class JacocoCov {

    public static int m (int i) {
        if (i >= 4 || i < 1) {
            i = 1;
        }
        System.out.println("i = " + i);

        for (int j = 0; j < 4; j++) {
            if (i == 1) {
                i = i + 10;
            }
            if (i == 2) {
                i = i + 20;
            }
            if (i == 3) {
                i = i + 30;
            }
        }
        System.out.println("i = " + i);
        return i;

    }
}
