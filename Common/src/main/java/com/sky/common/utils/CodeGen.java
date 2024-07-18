package com.sky.common.utils;

public class CodeGen {
    //生成指定位数的随机数
    public static int generatedCode(int count) {
        return (int)((Math.random()*9+1)* Math.pow(10,count-1));
    }
}
