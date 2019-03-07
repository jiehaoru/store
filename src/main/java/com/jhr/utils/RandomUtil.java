package com.jhr.utils;

import java.util.Random;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/07 16:37
 */
public class RandomUtil {

    private static final int MAX_CENTER_ID = 32;
    private static final int MAX_MACHINE_ID = 32;

    public RandomUtil() {
    }

    public static long randomCenterId() {
        return (long)(new Random((new Random()).nextLong())).nextInt(32);
    }

    public static long randomMachineID() {
        return (long)(new Random((new Random()).nextLong())).nextInt(32);
    }
}
