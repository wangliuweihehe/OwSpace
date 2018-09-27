package com.wlw.admin.owspace.app;


import java.util.regex.Pattern;

public class TestPattern {


    public static void main(String[] args) {
        new TestPattern().test1();
    }

    public void test1() {
        String str = "123198460555555555abc";
        //匹配多个数字[0-9]  $ 以什么结尾
        String patt = "^[0-9]+abc$";
//        System.out.println(str.matches(patt));
        Pattern pattern=Pattern.compile(patt);
        pattern.matcher(str);
        System.out.println(pattern.matcher(str));
    }
}
