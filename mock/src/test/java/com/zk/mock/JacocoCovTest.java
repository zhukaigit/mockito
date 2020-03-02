package com.zk.mock;

import org.junit.Assert;
import org.junit.Test;

public class JacocoCovTest {

    @Test
    public void test() {
        Assert.assertEquals(JacocoCov.m(1), 11);
    }
}
