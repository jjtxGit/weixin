package com.jjtx.weixin;

/**
 * Created by jjtx on 2016/10/8.
 */
public class MainTest {

    public void test1() {
        StringBuilder s = null;
        test2(s);
        System.out.println(s.toString());
    }

    public void test2(StringBuilder s) {
        s = new StringBuilder("hello world");
    }

}
