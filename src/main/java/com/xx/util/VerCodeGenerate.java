package com.xx.util;

import java.security.SecureRandom;
import java.util.Random;

public class VerCodeGenerate {
    //验证码包含的字段，可自己设置
    private static final String SYMBOLS = "0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();

    //    生成 6 位数的随机数字
    public static String generateVerCode() {
        //	如果是六位，就生成大小为 6 的数组
        char[] numbers = new char[6];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(numbers);
    }
}
